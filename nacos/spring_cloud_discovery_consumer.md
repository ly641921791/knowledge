Spring Cloud 服务消费 - Nacos
-

##### 前言

本文介绍`Spring Cloud`如何通过`Nacos`作为注册中心实现服务消费

Nacos服务搭建过程略，本文案例使用官网提供的服务，详细信息如下

- Nacos控制台
	- 地址 ：http://console.nacos.io/nacos/index.html
	- 账号/密码 ：nacos/nacos
- 客户端配置
	- 注册中心 ：spring.cloud.nacos.discovery.server-addr = console.nacos.io:80
	- 配置中心 ：spring.cloud.nacos.config.server-addr = console.nacos.io:80

##### 使用教程

###### 第一步 创建项目

创建Maven项目：`nacos-spring-cloud-discovery-consumer`

###### 第二步 加入依赖

``` xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
</dependencies>
```

通过`spring-cloud-starter-alibaba-nacos-discovery`引入依赖并实现自动配置

###### 第三步 修改配置

``` properties
server.port = 8082
spring.application.name = spring-cloud-discovery-consumer
spring.cloud.nacos.discovery.server-addr = console.nacos.io:80
```

###### 第四步 引导程序 & 消费服务

``` java
@EnableDiscoveryClient
@SpringBootApplication
public class NacosConsumerApplication {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApplicationRunner runner(){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                System.out.println(restTemplate.getForObject("http://spring-cloud-discovery-provider/hello/consumer", String.class));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }

}
```

通过`Spring Cloud`原生注解`@EnableDiscoveryClient`开启服务注册发现功能

通过`Spring Cloud`原生注解`@LoadBalanced`开启RestTemplate与Ribbon集成

###### 第五步 启动应用

启用应用后，控制台打印出`[spring-cloud-discovery-provider] : Hello , consumer`说明服务消费成功

##### 源码 & 参考

源码地址（可运行） ：https://github.com/ly641921791/knowledge-examples/tree/master/nacos-example/nacos-spring-cloud-discovery-consumer

参考文档 ：https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html