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

TODO 如何配置异步任务

> 示例代码 https://github.com/ly641921791/knowledge-examples/tree/master/spring-example/spring-task-async-complex

###### 常见问题

1. 异步任务失效

原因1：通过this调用的异步方法会失效，因为异步任务的原理是Aop拦截，this调用无法被拦截，可以通过修改配置Aop配置，通过AopContext得到当前对象的代理对象

原因2：通过new得到的对象异步方法失效，因为没有被Spring容器增强

原因3：`AsyncConfigurer`及子类中的异步方法失效，因为这些类在异步任务处理之前已经创建完成，无法得到二次处理

###### 源码解析

- 阅读源码必然从异步任务的开关`@EnableAsync`看起

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AsyncConfigurationSelector.class)
public @interface EnableAsync {
	// 具体属性省略
}

public class AsyncConfigurationSelector extends AdviceModeImportSelector<EnableAsync> {
	
	// 省略无关内容

	@Override
	@Nullable
	public String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
			case PROXY:
				return new String[] { ProxyAsyncConfiguration.class.getName() };
			case ASPECTJ:
				return new String[] { ASYNC_EXECUTION_ASPECT_CONFIGURATION_CLASS_NAME };
			default:
				return null;
		}
	}
}
```

可以看到在`@EnableAsync`注解上标注了`@Import(AsyncConfigurationSelector.class)`这样一行代码。所以`AsyncConfigurationSelector`的代码也顺便贴在上面。

> **@Import**注解的作用是向容器注册组件。在博客中有[详细介绍](https://blog.csdn.net/weixin_38229356/article/details/80699973)，不懂的同学可以点进去看看。

通过查看`AsyncConfigurationSelector`的源码，我们发现默认配置的情况下，`@EnableAsync`会通过`@Inport`注解向容器注册`ProxyAsyncConfiguration`的实例。

- 所以，接下来看看`ProxyAsyncConfiguration`的源码

```java
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyAsyncConfiguration extends AbstractAsyncConfiguration {

	@Bean(name = TaskManagementConfigUtils.ASYNC_ANNOTATION_PROCESSOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public AsyncAnnotationBeanPostProcessor asyncAdvisor() {
		// 内容省略
	}
}
```

通过查看源码，`AbstractAsyncConfiguration`向容器中注册了`AsyncAnnotationBeanPostProcessor`的实例，通过类名也能推测出该类的作用：标记了@Async注解的Bean的后置处理器。

> 后置处理器：Spring暴露出来的拓展点，对容器中产生的Bean进行改造，使其拥有我们配置的功能。

![avatar](http://www.plantuml.com/plantuml/png/dP8n3i8m34NtdY8Nw0qO6WRcIfp0f0P50hkoNI0IXoT2XI4PsfB9YT-pzQLqQY0stQvD94CvGq2tHw0316D9_W62HQjdjLuue2fB0oSroR3pSr7QVhX7ZNHPqBvOlI6Vwj7jNcsxmitLC_sknsaSMMojl4Xy1cqCNbB_pDX4pmiqKV14R62HgGxR8Gtv6xm1)

通过UML图，知道`AsyncAnnotationBeanPostProcessor`通过实现`BeanPostProcessor`接口得到了对Bean后置处理的功能，并且通过继承了`ProxyProcessorSupport`类得到了对bean进行代理处理的功能。

也就是异步方法的实现，是通过将容器中bean组件进行拦截处理，通过代理对象的方式得到了异步处理任务的功能。