[TOC]

# Spring Boot AOP 原理


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

