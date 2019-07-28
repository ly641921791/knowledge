[TOC]

# Spring Boot Json

　　Spring Boot 默认使用Jackson完成Object和Json的转换。主要体现在@ResponseBody和@RequestBody注解的使用。

   通过@ResponseBody和@RequestBody可以完成json格式参数的接受和返回。

## 自定义ObjectMapper

　　Jackson通过ObjectMapper完成Object和Json的转化，Spring Boot 默认提供了ObjectMapper，也可以使用自己的ObjectMapper覆盖Spring Boot中的提供。



```java
@RestController
@RequestMapping("/json")
public class JsonController {
    // 访问 http://localhost:8080/json/now
    // 输出 "2018-10-04T08:02:03.683+0000"
	@RequestMapping("/now")
	public Date now() {
		return new Date();
	}
}
```



```java
// 加入配置 输出 "2018-10-04 16:06:43"
@Configuration
public class JacksonConfig {
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		return objectMapper;
	}
}
```

## @JsonView




https://blog.csdn.net/zone_four/article/details/78355015
https://blog.csdn.net/gefangshuai/article/details/50328361
https://blog.csdn.net/zone_four/article/details/78355015


## @JsonComponents