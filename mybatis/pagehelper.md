分页插件
-

##### Spring Boot

官网推出的Starter，个人感觉使用不够自由灵活，因此不使用

依赖

``` xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.10</version>
</dependency>
```

配置分页插件

``` java
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 最好配置一下，某些情况获取数据信息存在问题，会导致空指针异常
        // PageAutoDialect#getDialect方法中，部分数据源获取URL为null，但是并没有做空校验
        properties.setProperty("helperDialect", "mysql");
        // 配置通过制定参数实现方法分页查询
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "pageNum=pageNum;pageSize=pageSize;");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
```

5.0.4手写COUNT语句支持 https://segmentfault.com/q/1010000013444285

> 官网文档 https://pagehelper.github.io/docs/howtouse/