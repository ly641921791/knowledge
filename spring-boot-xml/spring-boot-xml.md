

Spring中定义了HttpMessageConverter作为消息转换器。

Spring MVC中已经提供了MappingJackson2XmlHttpMessageConverter作为XML消息转换器



对于Spring项目，需要手动配置

```java
@Configuration
public class MessageConverterConfig1 extends WebMvcConfigurerAdapter {
	@Override
	public void
	configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
		builder.indentOutput(true);
		converters.add(new MappingJackson2XmlHttpMessageConverter(builder.build()));
	}
}
```

对于SpringBoot项目，加入下面依赖自动配置。（加入下面的依赖后，@ResponseBody默认XML格式）

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

Bean类添加注解

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "User")
public class User {
	@JacksonXmlProperty(localName = "id")
	private Integer id;
	@JacksonXmlProperty(localName = "name")
	private String name;
}
```

controller定义

```java
	@RequestMapping(value = "/user",
			consumes = MediaType.APPLICATION_XML_VALUE,
			produces = MediaType.APPLICATION_XML_VALUE
	)
	public User user(@RequestBody User user) {
		return user;
	}
```

参数及返回

```xml
<User>
	<id>1</id>
	<name>ly</name>
</User>
```