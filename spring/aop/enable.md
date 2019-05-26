开启AOP
-

###### XML开启AOP

配置文件中加入`</aop:aspectj-autoproxy>`节点

###### 注解开启AOP

在任意配置类加入`@EnableAspectJAutoProxy`注解，该注解有两个属性

- proxyTargetClass ：是否使用Cglib实现AOP功能，默认使用JDK动态代理实现AOP
- exposeProxy ：是否暴露代理对象，设置为true则可以通过`AopContext.currentProxy()`获得当前对象的代理对象

###### 自动配置AOP

Spring Boot项目，AOP默认开启，配置`spring.aop.auto=false`关闭AOP