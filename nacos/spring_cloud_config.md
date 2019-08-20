Spring Cloud 配置中心 - Nacos
-

##### 前言

本文介绍`Spring Cloud`如何通过`Nacos`作为配置中心

Nacos服务搭建过程略，本文案例使用官网提供的服务，详细信息如下

- Nacos控制台
	- 地址 ：http://console.nacos.io/nacos/index.html
	- 账号/密码 ：nacos/nacos
- 客户端配置
	- 注册中心 ：spring.cloud.nacos.discovery.server-addr = console.nacos.io:80
	- 配置中心 ：spring.cloud.nacos.config.server-addr = console.nacos.io:80

##### 使用教程

###### 第一步 创建项目

创建Maven项目：`nacos-spring-cloud-config`

###### 第二步 加入依赖

``` xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
</dependencies>
```

通过引入`spring-cloud-starter-alibaba-nacos-config`依赖实现自动配置

###### 第三步 配置文件

通过[Nacos Open API](https://nacos.io/zh-cn/docs/open-API.html)创建配置，dataId=spring-cloud-config-nacos.properties，内容为`useLocalCache=true`

``` shell
curl -X POST "http://console.nacos.io/nacos/v1/cs/configs?dataId=spring-cloud-config-nacos.properties&group=DEFAULT_GROUP&content=useLocalCache=true"
```

本地创建`bootstrap.properties`文件，并加入如下配置

``` properties
spring.application.name = spring-cloud-config-nacos

spring.cloud.nacos.config.server-addr = console.nacos.io:80
```

默契情况下，应用会请求dataId=${spring.application.name}.properties对应的内容作为配置

###### 第四步 引导程序

``` java
@SpringBootApplication
public class NacosConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class,args);
    }

}
```

###### 第五步 接口开发

``` java
@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value(value = "${useLocalCache:false}")
    private String useLocalCache;

    @RequestMapping("/get")
    public String get() {
        return useLocalCache;
    }

}

```

该接口用于测试配置获取以及配置更新

###### 第六步 启动测试

应用启动后，调用`curl http://localhost:8080/config/get`，返回true

通过[Nacos Open API](https://nacos.io/zh-cn/docs/open-API.html)修改配置

``` shell
curl -X POST "http://console.nacos.io/nacos/v1/cs/configs?dataId=spring-cloud-config-nacos.properties&group=DEFAULT_GROUP&content=useLocalCache=false"
```

再次访问`curl http://localhost:8080/config/get`，返回false

###### 源码 & 参考

源码地址（可运行）：https://github.com/ly641921791/knowledge-examples/tree/master/nacos-example/nacos-spring-cloud-config

参考文档 ：https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html

##### 配置加载规则

通过 DataId和Group 可以定位到唯一的配置

###### DataId

DataId 默认为 ${spring.cloud.nacos.config.prefix}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}

- spring.cloud.nacos.config.prefix ： 默认值是`spring.application.name`

- spring.profile.active ： 当该属性未配置时，DataId 为 ${spring.cloud.nacos.config.prefix}.${spring.cloud.nacos.config.file-extension}

- spring.cloud.nacos.config.file-extension ： 默认 properties

###### Group

对应的配置为`spring.cloud.nacos.config.group`， 默认 DEFAULT_GROUP

###### Namespace

对应的配置为`spring.cloud.nacos.config.namespace`

###### 多环境处理

官网建议，通过Namespace区分环境，通过group区分业务层
