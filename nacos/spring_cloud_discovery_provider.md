Spring Cloud 服务注册 - Nacos
-

##### 前言

本文介绍`Spring Cloud`如何通过`Nacos`作为注册中心实现服务注册

Nacos服务搭建过程略，本文案例使用官网提供的服务，详细信息如下

- Nacos控制台
	- 地址 ：http://console.nacos.io/nacos/index.html
	- 账号/密码 ：nacos/nacos
- 客户端配置
	- 注册中心 ：spring.cloud.nacos.discovery.server-addr = console.nacos.io:80
	- 配置中心 ：spring.cloud.nacos.config.server-addr = console.nacos.io:80

##### 使用教程

###### 第一步 创建项目

创建Maven项目：`nacos-spring-cloud-discovery-provider`

###### 第二步 加入依赖

``` xml
<dependencys>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
</dependencys>
```

通过`spring-cloud-starter-alibaba-nacos-discovery`引入依赖并实现自动配置

###### 第三步 修改配置 

``` properties
server.port = 8081
spring.application.name = spring-cloud-discovery-provider
spring.cloud.nacos.discovery.server-addr = console.nacos.io:80
```

###### 第四步 引导程序

``` java
@EnableDiscoveryClient
@SpringBootApplication
public class NacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApplication.class,args);
    }

}
```

通过`Spring Cloud`原生注解`@EnableDiscoveryClient`开启服务注册发现功能

###### 第五步 接口开发

``` java
@RestController
public class HelloController {

    @Value("${spring.application.name}")
    private String serviceName;

    @RequestMapping("/hello/{name}")
    public String sayHello(@PathVariable String name) {
        return String.format("[%s] : Hello , %s", serviceName, name);
    }

}
```

开发一个简单接口，方便服务消费测试

###### 第六步 启动应用

启动应用后，在Nacos控制台的服务管理-服务列表页面找到服务名为`spring-cloud-discovery-provider`的服务，说明注册成功

##### 注册原理

@EnableDiscoveryClient

DiscoveryClient

服务注册

服务获取

服务续约

##### 源码 & 参考

源码地址（可运行） ：https://github.com/ly641921791/knowledge-examples/tree/master/nacos-example/nacos-spring-cloud-discovery-provider

参考文档 ：https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html