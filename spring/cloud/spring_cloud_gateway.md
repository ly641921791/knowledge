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

``` java
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
```

配置转发

``` yaml
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


##### Predicate

Predicate 是一个布尔函数，详细的定义可以看[这里](https://www.zhihu.com/question/25404942/answer/53680068)，当返回true时，
表示匹配当前路由

###### 匹配路径

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: host_route
        uri: https://example.org
        predicates:
        - Path=/foo/{segment},/bar/{segment}
```

URI参数变量在过滤器中使用

###### 匹配日期

日期之前

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: before_route
        uri: https://example.org
        predicates:
        - Before=2017-01-20T17:42:47.789-07:00[America/Denver]
```

日期之后

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: after_route
        uri: https://example.org
        predicates:
        - After=2017-01-20T17:42:47.789-07:00[America/Denver]
```

日期之间

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: between_route
        uri: https://example.org
        predicates:
        - Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]
```

###### 匹配Cookie

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: cookie_route
        uri: https://example.org
        predicates:
        - Cookie=cookieName, cookieValueRegex
```

###### 匹配Header

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: header_route
        uri: https://example.org
        predicates:
        - Header=headerName, headerValueRegex
```

###### 匹配Host

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: host_route
        uri: https://example.org
        predicates:
        - Host=**.somehost.org,**.anotherhost.org
```

还可以使用URI参数变量匹配，如：{sub}.myhost.org ，{sub}的值可以在过滤器中使用

###### 匹配Method

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: method_route
        uri: https://example.org
        predicates:
        - Method=GET
```

###### 匹配参数

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: query_route
        uri: https://example.org
        predicates:
        - Query=paramName1
        - Query=paramName2,paramValue2Regex
```

###### 匹配IP

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: remoteaddr_route
        uri: https://example.org
        predicates:
        - RemoteAddr=192.168.1.1/24
```

###### 负载均衡 - 权重

``` yaml
spring:
  cloud:
    gateway:
      routes:
      - id: weight_high
        uri: https://weighthigh.org
        predicates:
        # 80%的流量
        - Weight=group1, 8
      - id: weight_low
        uri: https://weightlow.org
        predicates:
        # 20%的流量
        - Weight=group1, 2
```



###### 相关文档

官网文档 https://cloud.spring.io/spring-cloud-gateway/reference/html/