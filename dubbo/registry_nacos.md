注册中心 - Nacos
-

###### 前言

本文介绍如何通过Nacos作为配置中心，实现Dubbo服务的注册与消费

以HelloService服务为例

```java
public interface HelloService {
    String sayHello(String name);
}
```

服务提供方和消费方都引入以下jar

```xml
<dependencys>

    <!-- Spring Boot dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-spring-boot-starter</artifactId>
    </dependency>

    <!-- Dubbo -->
    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo</artifactId>
    </dependency>

    <!-- Dubbo Registry Nacos -->
    <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-registry-nacos</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba.nacos</groupId>
        <artifactId>nacos-client</artifactId>
    </dependency>
</dependencys>
```

需要搭建一个Nacos服务，这里直接使用[程序员DD](http://blog.didispace.com/)提供的公益性的Nacos服务，
[详细地址](http://blog.didispace.com/open-nacos-server-1-0-0/)

###### 服务提供方

1. 实现接口

```java
@Service(version = "${dubbo.service.version}")
public class HelloServiceImpl implements HelloService {

    @Value("${spring.application.name}")
    private String serviceName;

    public String sayHello(String name) {
        return String.format("[%s] : Hello , %s", serviceName, name);
    }

}
```

2. 配置服务

```properties
spring.application.name = nacos-dubbo-provider

dubbo.scan.base-packages= com.github.ly641921791.dubbo.example.nacos.provider.service

dubbo.protocol.name     = dubbo
dubbo.protocol.port     = -1

# dubbo.registry.address  = nacos://127.0.0.1:8848
dubbo.registry.address  = nacos://nacos.didispace.com:80

dubbo.service.version   = 1.0.0
```

3. 引导程序

```java
@SpringBootApplication
public class NacosProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApplication.class, args);
    }
}
```

4. 启动应用

启动后在Nacos控制台发现应用注册成功，服务名为`providers:com.github.ly641921791.dubbo.example.common.service.HelloService:1.0.0`

###### 服务消费方

1. 配置服务

```properties
spring.application.name = nacos-dubbo-consumer

# dubbo.registry.address  = nacos://127.0.0.1:8848
dubbo.registry.address  = nacos://nacos.didispace.com:80

dubbo.service.version   = 1.0.0
```

2. 引导程序 & 服务消费

```java
@SpringBootApplication
public class NacosConsumerApplication {

    @Reference(version = "${dubbo.service.version}")
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args).close();
    }

    @Bean
    public ApplicationRunner runner() {
        return new ApplicationRunner() {
            public void run(ApplicationArguments args) throws Exception {
                System.out.println(helloService.sayHello("consumer"));
            }
        };
    }
}
```

3. 启动程序

控制台打印出`[nacos-provider] : Hello , consumer`，说明消费成功

###### 源码及参考

源码地址（可运行） ：https://github.com/ly641921791/knowledge-examples/tree/master/dubbo-example

- 服务提供模块 ：nacos-dubbo-provider
- 服务消费模块 ：nacos-dubbo-consumer

参考文档（Nacos官网） ：http://dubbo.apache.org/zh-cn/docs/user/references/registry/nacos.html

参考案例（Nacos示例） ：https://github.com/apache/dubbo-spring-boot-project/tree/master/dubbo-spring-boot-samples/dubbo-registry-nacos-samples


