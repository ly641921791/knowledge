开启AOP
-

###### XML开启AOP

先引入AOP的命名空间

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans  http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.0.xsd
           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>

</beans>
```

配置文件中加入`</aop:aspectj-autoproxy>`节点，该节点有两个属性

- proxy-target-class ：是否使用Cglib实现AOP功能，默认使用JDK动态代理实现AOP
- expose-proxy ：是否暴露代理对象，设置为true则可以通过`AopContext.currentProxy()`获得当前对象的代理对象

###### 注解开启AOP

在任意配置类加入`@EnableAspectJAutoProxy`注解，该注解有两个属性

- proxyTargetClass ：是否使用Cglib实现AOP功能，默认使用JDK动态代理实现AOP
- exposeProxy ：是否暴露代理对象，设置为true则可以通过`AopContext.currentProxy()`获得当前对象的代理对象

###### 自动配置AOP

Spring Boot项目，AOP默认开启，配置`spring.aop.auto=false`关闭AOP

通过`AopAutoConfiguration`类引入`@EnableAspectJAutoProxy`实现，默认配置`@EnableAspectJAutoProxy(proxyTargetClass = false)`