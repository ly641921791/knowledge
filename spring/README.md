Spring
-

- IoC
	- [应用配置](ioc/configuration.md) 
	- [依赖注入](ioc/dependency_injection.md)
    - [源码分析 - AbstractBeanFactory](ioc/AbstractBeanFactory.md)

- Web
	- [异常处理](web/exception.md)
	- [参数校验](web/validate.md)
	- [过滤器](web/filter.md)
	- [静态资源](web/static_content.md)
	- [响应压缩](web/compression.md)
	- [WebSocket](web/websocket.md)
	- [RestTemplate使用](web/RestTemplate.md)
	- [模本引擎 Velocity](https://www.kancloud.cn/boshu/springboot/215852)
	- [发布War](web/deploy_war.md)

- Cache
	- [使用缓存](cache/use.md)

- Boot
    - [Spring Boot 启动过程（一）SpringBootApplication](boot/SpringBootApplication.md)
    - [Spring Boot 启动过程（二）SpringApplication](boot/SpringApplication.md)

- Task
	- [XML配置](task/task_xml.md)
	- [几种任务实现的对比](https://blog.csdn.net/wqh8522/article/details/79224290)

###### ApplicationRunner & CommandLineRunner

SpringBoot项目中，started和running状态之间会执行这两个类，详情查看SpringApplication#run方法




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
