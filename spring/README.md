Spring
-

- IoC
	- [应用配置](ioc/configuration.md) 

- Web
	- [静态资源](web/static_content.md)
	
- Cache
	- [使用缓存](cache/use.md)







## 声明式事物

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

BeanDefinitionRegistry

```java
BeanDefinitionRegistry registry;

//方法1
RootBeanDefinition bean = new RootBeanDefinition(String.class)
//方法2
AbstractBeanDefinition bean = 	BeanDefinitionBuilder.rootBeanDefinition(String.class).getBeanDefinition();

registry.registryBeanDefinition("hello",bean);
```

AliasFor注解使用及原理
https://www.jianshu.com/p/869ed7037833
