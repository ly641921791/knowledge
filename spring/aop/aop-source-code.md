[TOC]

# Spring Boot AOP 原理

## 1 AopAutoConfiguration

**源码**

```java
@Configuration
@ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class,AnnotatedElement.class })
@ConditionalOnProperty(prefix = "spring.aop", name = "auto", havingValue = "true", matchIfMissing = true)
public class AopAutoConfiguration {

	@Configuration
	@EnableAspectJAutoProxy(proxyTargetClass = false)
	@ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false", matchIfMissing = false)
	public static class JdkDynamicAutoProxyConfiguration { }

	@Configuration
	@EnableAspectJAutoProxy(proxyTargetClass = true)
	@ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = true)
	public static class CglibAutoProxyConfiguration { }

}
```

**解读**

1. 配置类
2. 默认情况下，当项目中存在AOP依赖包时生效
3. 默认情况下，当配置类生效，开启`@EnableAspectJAutoProxy(proxyTargetClass = true)`

## 2 EnableAspectJAutoProxy

**源码**

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

**解读**

向容器注册`AspectJAutoProxyRegistrar`

## 3 AspectJAutoProxyRegistrar

**源码**

```java
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		if (enableAspectJAutoProxy != null) {
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}
}
```

**解读**

1. 注册`AnnotationAwareAspectJAutoProxyCreator`
2. 根据`@EnableAspectJAutoProxy`的属性设置`AnnotationAwareAspectJAutoProxyCreator`的属性

## 4 AnnotationAwareAspectJAutoProxyCreator

**源码**

``` sequence
title:111111
participant AnnotationAwareAspectJAutoProxyCreator
participant AspectJAwareAdvisorAutoProxyCreator
participant AbstractAdvisorAutoProxyCreator
participant AbstractAutoProxyCreator


AspectJAwareAdvisorAutoProxyCreator->AspectJAwareAdvisorAutoProxyCreator:2
AspectJAwareAdvisorAutoProxyCreator-->AspectJAwareAdvisorAutoProxyCreator:4
note left of AspectJAwareAdvisorAutoProxyCreator:3

```

