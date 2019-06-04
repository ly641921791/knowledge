异步任务
-

Spring 异步任务涉及到两个注解

- @EnableAsync 标记在配置类上，开启异步任务
- @Async 标记方法，将方法增强为异步方法

###### 简单示例

1. 异步任务执行类

```java
@Slf4j
@Component
public class SimpleAsyncTask {

    @Async
    public void execute() {
        log.info("【任务执行】当前线程：{}", Thread.currentThread().getName());
    }

}
```

2. 引导程序

```java
@Slf4j
@EnableAsync
@SpringBootApplication
public class SimpleAsyncTaskApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SimpleAsyncTaskApplication.class, args);
        log.info("【程序启动】当前线程：{}", Thread.currentThread().getName());
        applicationContext.getBean(SimpleAsyncTask.class).execute();
    }

}
```

3. 运行程序

控制台打印出如下日志，说明任务执行并没有使用主线程执行，异步任务开启成功

```log
2019-06-03 21:26:33.862  INFO 1150 --- [           main] c.g.l.s.t.a.s.SimpleAsyncTaskApplication : 【程序启动】当前线程：main
2019-06-03 21:26:33.885  INFO 1150 --- [         task-1] c.g.l.s.t.async.simple.SimpleAsyncTask   : 【任务执行】当前线程：task-1
```

> 示例代码 https://github.com/ly641921791/knowledge-examples/tree/master/spring-example/spring-task-async-simple

###### 复杂示例

Spring在没有找到线程池时会创建默认的线程池用于处理异常任务，对于异常的任务默认不处理

Spring提供了`AsyncConfigurer`接口用于配置异步任务使用的线程池和异常处理规则，代码如下

```java
public interface AsyncConfigurer {
	@Nullable
	default Executor getAsyncExecutor() {
		return null;
	}

	@Nullable
	default AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
}
```

实现`AsyncConfigurer`接口，并将实现类注册到Spring容器就可以达到配置的目的，示例如下

```java
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
	// 实现略
}
```

> 示例代码 https://github.com/ly641921791/knowledge-examples/tree/master/spring-example/spring-task-async-complex

###### 常见问题

1. 异步任务失效

原因1：通过this调用的异步方法会失效，因为异步任务的原理是Aop拦截，this调用无法被拦截，可以通过修改配置Aop配置，通过AopContext得到当前对象的代理对象

原因2：通过new得到的对象异步方法失效，因为没有被Spring容器增强

原因3：`AsyncConfigurer`及子类中的异步方法失效，因为这些类在异步任务处理之前已经创建完成，无法得到二次处理

###### 源码解析

1. @EnableAsync

@EnableAsync通过@Import引入了`AsyncConfigurationSelector`，代码如下

```java
@Import(AsyncConfigurationSelector.class)
public @interface EnableAsync {
}
```

2. AsyncConfigurationSelector

```java
public class AsyncConfigurationSelector extends AdviceModeImportSelector<EnableAsync> {

	public String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return new String[] {ProxyAsyncConfiguration.class.getName()};
			case ASPECTJ:
				return new String[] {ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME};
			default:
				return null;
		}
	}

}
```

AdviceMode是@EnableAsync的一个属性，默认值是`AdviceMode.PROXY`，也就是默认情况下，会引入`ProxyAsyncConfiguration`

3. ProxyAsyncConfiguration

`ProxyAsyncConfiguration`做了三件事，代码如下

- 通过`setImportMetadata`方法得到了@EnableAsync注解的属性信息，
- 通过`setConfigurers`方法得到了`AsyncConfigurer`配置的线程池和异常处理器，
- 通过`asyncAdvisor`方法配置并引入了`AsyncAnnotationBeanPostProcessor`

```java
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyAsyncConfiguration extends AbstractAsyncConfiguration {

	/*  继承自 AbstractAsyncConfiguration
		@Nullable
		protected AnnotationAttributes enableAsync;
	
		@Nullable
		protected Supplier<Executor> executor;
	
		@Nullable
		protected Supplier<AsyncUncaughtExceptionHandler> exceptionHandler;
	
		@Override
		public void setImportMetadata(AnnotationMetadata importMetadata) {
			this.enableAsync = AnnotationAttributes.fromMap(
					importMetadata.getAnnotationAttributes(EnableAsync.class.getName(), false));
			if (this.enableAsync == null) {
				throw new IllegalArgumentException(
						"@EnableAsync is not present on importing class " + importMetadata.getClassName());
			}
		}
	
		@Autowired(required = false)
		void setConfigurers(Collection<AsyncConfigurer> configurers) {
			if (CollectionUtils.isEmpty(configurers)) {
				return;
			}
			if (configurers.size() > 1) {
				throw new IllegalStateException("Only one AsyncConfigurer may exist");
			}
			AsyncConfigurer configurer = configurers.iterator().next();
			this.executor = configurer::getAsyncExecutor;
			this.exceptionHandler = configurer::getAsyncUncaughtExceptionHandler;
		}
	*/

	@Bean(name = TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public AsyncAnnotationBeanPostProcessor asyncAdvisor() {
		Assert.notNull(this.enableAsync, "@EnableAsync annotation metadata was not injected");
		AsyncAnnotationBeanPostProcessor bpp = new AsyncAnnotationBeanPostProcessor();
		bpp.configure(this.executor, this.exceptionHandler);
		Class<? extends Annotation> customAsyncAnnotation = this.enableAsync.getClass("annotation");
		if (customAsyncAnnotation != AnnotationUtils.getDefaultValue(EnableAsync.class, "annotation")) {
			bpp.setAsyncAnnotationType(customAsyncAnnotation);
		}
		bpp.setProxyTargetClass(this.enableAsync.getBoolean("proxyTargetClass"));
		bpp.setOrder(this.enableAsync.<Integer>getNumber("order"));
		return bpp;
	}

}
```

4. AsyncAnnotationBeanPostProcessor

`AsyncAnnotationBeanPostProcessor`

- 通过`setBeanFactory`方法创建了`AsyncAnnotationAdvisor`
- 通过`postProcessAfterInitialization`创建了Bean的代理对象，并将`AsyncAnnotationAdvisor`作为Advisor，

`AsyncAnnotationAdvisor`将`AnnotationAsyncExecutionInterceptor`作为方法拦截器，拦截目标方法执行，将其交过线程池执行实现异步调用

