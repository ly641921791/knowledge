Spring Cloud Gateway
-

Spring Cloud Gateway 是Spring官方推出的网关组件，目的是代替Zuul

Spring Cloud Gateway 相比于Zuul，使用Reactor技术实现，支持长连接

依赖

``` xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

引导程序

```java
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
```

配置转发

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: route1
          uri: https://cloud.spring.io
          predicates:
          - Path=/**
```

- id ：唯一路由ID
- uri ：目标服务器地址
- predicates ：路由条件

当前配置会将所有的请求都转发到 https://cloud.spring.io，例如：http://127.0.0.1:8080/spring-cloud-gateway/reference/html/转发到https://cloud.spring.io/spring-cloud-gateway/reference/html/

代码配置转发规则

``` java
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("route1", predicateSpec -> predicateSpec.path("/spring-cloud-gateway/reference/html/").uri("https://cloud.spring.io"))
                .build();
    }
```

效果与上面相同

> https://cloud.spring.io/spring-cloud-gateway/reference/html/