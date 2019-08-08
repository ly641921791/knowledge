Spring cache
-

Spring Cache


@CacheEvent







###### 解决方案：强转异常

使用热部署插件时，使用了不同的类加载器加载修改前后的代码，导致同一个类的类加载器不同，引起强转失败，关闭热部署插件

[Spring Boot缓存实战 Redis 设置有效时间和自动刷新缓存-2](https://www.jianshu.com/p/e53c1b60c6e1)

> 官网文档 https://docs.spring.io/spring/docs/5.1.9.RELEASE/spring-framework-reference/integration.html#cache