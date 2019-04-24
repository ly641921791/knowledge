Spring Boot Aop原理



首先查看自动配置类源码

``` java
@Configuration
@ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class,AnnotatedElement.class })
@ConditionalOnProperty(prefix = "spring.aop", name = "auto", havingValue = "true", matchIfMissing = true)
public class AopAutoConfiguration {

	@Configuration
	@EnableAspectJAutoProxy(proxyTargetClass = false)
	@ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false", matchIfMissing = false)
	public static class JdkDynamicAutoProxyConfiguration {}
	
	@Configuration
	@EnableAspectJAutoProxy(proxyTargetClass = true)
	@ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = true)
	public static class CglibAutoProxyConfiguration {}
    
}

```



类上标注的三个注解有以下含义：

1. 该类是一个配置类
2. 该类生效的条件
   1. 存在指定的四个类
   2. 当`spring.aop.auto=true`或不写该配置

该类中两个方法标记的注解类似，含义如下：

1. 当且仅当`spring.aop.proxy-target-class=false`时，`	@EnableAspectJAutoProxy(proxyTargetClass = false)`生效，即使用JDK动态代理
2. 当`spring.aop.proxy-target-class=false`或不写该配置时，`	@EnableAspectJAutoProxy(proxyTargetClass = false)`生效，即使用Cglib动态代理



然后查看@EnableAspectJAutoProxy源码

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {

	boolean proxyTargetClass() default false;

	boolean exposeProxy() default false;
}
```

这个注解只有一个作用，导入`AspectJAutoProxyRegistrar`类。







Aop



开启

​	@EnableAspectJAutoProxy

原理

​	利用`@Import(AspectJAutoProxyRegistrar.class) `向容器中注入`AnnotationAwareAspectJAutoProxyCreator `

​	

​	`AnnotationAwareAspectJAutoProxyCreator `实现了后置处理器和自动装配Bean工厂接口







##声明式事物







# 拓展原理

### BeanPostProcessor

- bean后置处理器
- bean创建对象初始化前后进行拦截

### BeanFactoryPostProcessor

- beanFactory后置处理器
- 在beanFactory标准初始化后调用,所有的bean定义已经保存加载到beanFactory,但bean未创建对象。

### BeanDefinitionRegistryPostProcessor

- bean注册中心后置处理器
- 继承了BeanFactoryPostProcessor
- 在bean定义信息将被加载，但bean实例未创建。在BeanFactoryPostProcessor之前使用

###监听器

### - ApplicationListener

### - @EventListener









BeanDefinitionRegisiry

```java
BeanDefinitionRegisiry registry;

//方法1
RootBeanDefinition bean = new RootBeanDefinition(String.class)
//方法2
AbstractBeanDefinition bean = 	BeanDefinitionBuilder.rootBeanDefinition(String.class).getBeanDefinition();

registry.registryBeanDefinition("hello",bean);
```





