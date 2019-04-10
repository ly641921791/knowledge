Nacos
-

Git https://github.com/alibaba/nacos

### 安装启动

下载地址： https://github.com/alibaba/nacos/releases

本文版本： 0.8.0

下载完成后，解压。进入bin目录执行脚本启动单机服务

- Linux/Unix/Mac： sh startup.sh -m standalone
- Windows： cmd startup.cmd -m standalone

启动完成后，访问： http://127.0.0.1:8848/nacos/，可以进入服务管理页面

### 接入服务

服务提供者和服务消费者都作为服务接入到Nacos服务中，接入过程相同

*第一步*：创建应用

创建Spring Boot应用

*第二步*：加入依赖

```xml
<project>        
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.5.RELEASE</version>
    </parent>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>0.2.1.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.SR2</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
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
</project>
``` 

*第三步*：标记注解

启动类标记@EnabledDiscoveryClient注解

*第四步*：修改配置

配置服务名和Nacos服务地址

```property
spring.application.name=nacos-producer
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```

*第五步*：启动应用

在Nacos服务列表可以发现刚刚启动的服务

### 消费服务

Spring Cloud提供了多种消费方式

- LoadBalancerClient 获取服务实例，手动消费
- RestTemplate 通过容器增强后，使用服务名消费
- WebClient 类似RestTemplate（Spring 5引入的，可理解为reactive版的RestTemplate）
- Feign 通过Feign消费

*LoadBalancerClient*

```java
@RestController
public class ConsumerController {
    
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    
    @GetMapping("/consumer")
    public String consumer(){
		ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-producer");
		String url = serviceInstance.getUri() + "/hello?name=consumer";
		return new RestTemplate().getForObject(url, String.class);
    }
    
}
```

*RestTemplate*

```java
@RestController
public class ConsumerController {
    
    @Autowired
	private RestTemplate restTemplate;
    
    @GetMapping("/consumer")
    public String consumer(){
        return restTemplate.getForObject("http://nacos-producer/hello?name=consumer",String.class);
    }
    
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

*WebClient*

```java
@RestController
public class ConsumerController {
    
    @Autowired
	private WebClient.Builder webClientBuilder;
    
    @GetMapping("/consumer")
    public Mono<String> consumer(){
        return webClientBuilder.build()
	        .get()
	        .uri("http://nacos-producer/hello?name=consumer")
	        .retrieve()
	        .bodyToMono(String.class);
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder(){
        return WebClient.builder();
    }
}
```

*Feign*

引入依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

```java
@RestController
@EnableFeignClients
public class ConsumerController {
    
    @Autowired
	private ProducerClient producerClient;
    
    @GetMapping("/consumer")
    public String consumer(){
        return producerClient.hello("consumer");
    }
    
    @FeignClient("nacos-producer")
    interface ProducerClient {
        
        @GetMapping("/hello")
        String hello(@RequestParam(name="name") String name);
        
    }
}
```

### 配置中心

