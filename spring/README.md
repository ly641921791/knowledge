Spring
-

- IoC
	- [应用配置](ioc/configuration.md) 
	- [依赖注入](ioc/dependency_injection.md)
	- [依赖注入 - JSR330](https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/core.html#beans-standard-annotations)
    - [源码分析 - AbstractBeanFactory](ioc/AbstractBeanFactory.md)

- Boot
    - [Spring Boot 启动过程（一）SpringBootApplication](boot/SpringBootApplication.md)
    - [Spring Boot 启动过程（二）SpringApplication](boot/SpringApplication.md)
    - [监控应用 - Actuator](boot/actuator.md)

- Spring Core
    - [Spring Validation](core/validation.md)

- Spring Data Access
    - [JdbcTemplate](data-access/JdbcTemplate.md)
    - [Transaction](data-access/transaction.md)
    - [Transaction Source Code](data-access/transaction_sc.md)

- Spring Integration
    - [Email](integration/email.md)
	- [Task - XML配置](task/task_xml.md)
	- [Task - async](task/async.md)
	- [几种任务实现的对比](https://blog.csdn.net/wqh8522/article/details/79224290)
	- [Cache](integration/cache.md)

- [Spring Aop 专题](aop/README.md)
- [Spring MVC 专题](web/README.md)
- [Spring Cloud 专题](cloud/README.md)


[SpringBoot + Mybatis + Druid + PageHelper 实现多数据源并分页](https://www.cnblogs.com/xuwujing/p/8964927.html)


###### ApplicationRunner & CommandLineRunner

SpringBoot项目中，started和running状态之间会执行这两个类，详情查看SpringApplication#run方法





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

AliasFor注解使用及原理
https://www.jianshu.com/p/869ed7037833

###### XML -> BeanDefinition

AbstractRefreshableApplicationContext#refreshBeanFactory中调用loadBeanDefinitions

BeanDefinitionRegistry

RootBeanDefinition beanDefinition = new RootBeanDefinition();
// 类型
beanDefinition.setBeanClass(GroupSequence.class);
// 初始化方法
beanDefinition.setInitMethodName("init");
// 范围
beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
// 装配类型
beanDefinition.setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_NAME);
// 对应 <property name="" ref="">
beanDefinition.getPropertyValues().addPropertyValue("sequenceDao", new RuntimeBeanReference("sequenceDao"));
// 对应 <property name="" value="">
beanDefinition.getPropertyValues().addPropertyValue("name", new TypedStringValue(beanName));
// 注册
registry.registerBeanDefinition(beanName, beanDefinition);
