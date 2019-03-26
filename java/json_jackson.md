Jackson
-

Jackson通过ObjectMapper实现POJO和JSON的转换，并且支持两种方式解析JSON。

- POJO Model
- Tree Model

## 两种解析方式

### POJO Model

最常用的解析方式，配合POJO解析JSON字符串。代码如下

```java
public class Jackson {
	@Data
	public static class PojoModelClass {
		private Integer id;
		private String name;
	}

	@Test
	public void pojoModelTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"id\":1,\"name\":\"ly\"}";
		PojoModelClass pojoModelClass = objectMapper.readValue(json, PojoModelClass.class);
		// 解析JSON得到POJO对象：   Jackson.DataBindClass(id=1, name=ly)
		System.out.println(pojoModelClass);
		// 将POJO序列化JSON：      {"id":1,"name":"ly"}
		System.out.println(objectMapper.writeValueAsString(pojoModelClass));
	}
}
```

### Tree Model

用于没有POJO时，解析JSON。代码如下

```java
public class Jackson {
	@Test
	public void treeModelTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"id\":1,\"name\":\"ly\"}";
		JsonNode jsonNode = objectMapper.readTree(json);
		// 输出    1
		System.out.println(jsonNode.get("id").asInt());
		// 输出    ly
		System.out.println(jsonNode.get("name").asText());
	}
}
```

## 其他功能

### 修改JSON KEY

通过`@JsonProperty`可以修改指定属性序列化和反序列化时JSON的KEY。代码如下

```java
public class Jackson {
    // 通过@JsonProperty将id-Id和name-Name做映射
    @Data
	public static class JsonPropertyClass {
		@JsonProperty("Id")
		private Integer id;
		@JsonProperty("Name")
		private String name;
	}
    
	@Test
	public void jsonPropertyTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"Id\":1,\"Name\":\"ly\"}";
		JsonPropertyClass jsonPropertyClass = objectMapper.readValue(json, JsonPropertyClass.class);
        // 输出    Jackson.JsonPropertyClass(id=1, name=ly)
		System.out.println(jsonPropertyClass);
        // 输出    {"Id":1,"Name":"ly"}
		System.out.println(objectMapper.writeValueAsString(jsonPropertyClass));
	}
}
```

### 忽视指定属性

在需要忽视的属性上标记`@JsonIgnore`，序列化和反序列化时会忽视该属性。代码如下

```java
public class Jackson {
	@Data
	public static class JsonIgnoreClass {
		private String username;
        // 序列化和反序列化都忽视password属性
		@JsonIgnore
		private String password;
	}

	@Test
	public void jsonIgnoreTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"username\":\"ly\",\"password\":\"123456\"}";
		JsonIgnoreClass jsonIgnoreClass = objectMapper.readValue(json, JsonIgnoreClass.class);
        // 输出    Jackson.JsonIgnoreClass(username=ly, password=null)
		System.out.println(jsonIgnoreClass);
		jsonIgnoreClass.setPassword("123456");
        // 输出    {"username":"ly"}
		System.out.println(objectMapper.writeValueAsString(jsonIgnoreClass));
	}
}
```

### 忽视一组属性

通过`@JsonIgnorePropertys`可以忽视一组属性。代码如下

```java
public class Jackson {
    @Data
	// 忽视id和password
	@JsonIgnoreProperties({"id", "password"})
	public static class JsonIgnorePropertiesClass {
		private Integer id;
		private String username;
		private String password;
	}
    
	@Test
	public void jsonIgnoreProperties() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"id\":1,\"username\":\"ly\",\"password\":\"123456\"}";
		JsonIgnorePropertiesClass jsonIgnorePropertiesClass = objectMapper.readValue(json, JsonIgnorePropertiesClass.class);
		// 输出    Jackson.JsonIgnorePropertiesClass(id=null, username=ly, password=null)
		System.out.println(jsonIgnorePropertiesClass);
		jsonIgnorePropertiesClass.setId(1);
		jsonIgnorePropertiesClass.setPassword("123456");
		// 输出    {"username":"ly"}
		System.out.println(objectMapper.writeValueAsString(jsonIgnorePropertiesClass));
	}
}
```

### 处理没有的属性

通过`@JsonAnySetter`和`@JsonAnyGetter`，可以处理POJO中没有的属性。代码如下

```java
public class Jackson {
	@Data
	public static class OtherPropertyClass {
		@JsonIgnore
		private Map<String, Object> anyProperty = new HashMap<>();

		// 反序列化时，JSON的KEY找不到的属性调用该方法
		@JsonAnySetter
		private void otherProperty(String k, Object v) {
			anyProperty.put(k, v);
		}

		// 序列化时，将返回map的每一对k-v序列化
		@JsonAnyGetter
		private Map<String, Object> otherProperty() {
			return anyProperty;
		}
	}

	@Test
	public void anyProperties() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"id\":1,\"username\":\"ly\",\"password\":\"123456\"}";
		OtherPropertyClass user = objectMapper.readValue(json, OtherPropertyClass.class);
        // 输出    Jackson.OtherPropertyClass(anyProperty={password=123456, id=1, username=ly})
		System.out.println(user);
        // 输出    {"password":"123456","id":1,"username":"ly"}
		System.out.println(objectMapper.writeValueAsString(user));
	}
}
```

### 指定命名策略

通过`@JsonNaming`可以指定整个类属性的命名策略

```java
public class Jackson {
	@Data
    // 指定驼峰转下划线命名策略
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	public static class JsonNamingClass {
		private String userName;
	}

	@Test
	public void jsonNamingTest() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"user_name\":\"ly\"}";
		JsonNamingClass jsonNamingClass = objectMapper.readValue(json, JsonNamingClass.class);
		// 输出    Jackson.JsonNamingClass(userName=ly)
		System.out.println(jsonNamingClass);
		// 输出    {"user_name":"ly"}
		System.out.println(objectMapper.writeValueAsString(jsonNamingClass));
	}
}
```

### 自定义序列化

通过`@JsonSerialize`和`@JsonDeserialize`注解可以自定义序列化规则

```java
public class Jackson {
	@Data
    // 指定序列化类
	@JsonSerialize(using = UserSerializer.class)
    // 指定反序列化类
	@JsonDeserialize(using = UserDeserializer.class)
	public static class User {
		private Integer id;
		private String name;
	}

	public static class UserSerializer extends JsonSerializer<User> {
		@Override
		public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeStartObject();
			gen.writeNumberField("field1", value.getId());
			gen.writeStringField("field2", value.getName());
			gen.writeEndObject();
		}
	}

	public static class UserDeserializer extends JsonDeserializer<User> {
		@Override
		public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonNode node = p.getCodec().readTree(p);
			User user = new User();
			user.setId(node.get("field1").asInt());
			user.setName(node.get("field2").asText());
			return user;
		}
	}

	@Test
	public void serializer() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		User user = new User();
		user.setId(1);
		user.setName("ly");
		String json = objectMapper.writeValueAsString(user);
		// 输出    {"field1":1,"field2":"ly"}
		System.out.println(json);
		// 输出    Jackson.User(id=1, name=ly)
		System.out.println(objectMapper.readValue(json, User.class));
	}
}
```

### 序列化组

通过`@JsonView`可以为一个类定义定义多个序列化属性组

## 常见异常分析

### com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException

https://blog.csdn.net/huanghanqian/article/details/80866287

@JsonInclude(value=Include.NON_NULL) :用在实体类的方法类的头上  作用是实体类的参数查询到的为null的不显示
@JSONField(name=”resType”)和 @JSONField(format=”yyyy-MM-dd”) 
枚举转换 https://www.cnblogs.com/binz/p/9178988.html

### 日期格式化

#### 设置全局日期格式化规则

通过`ObjectMapper`的`setDateFormat`方法可以设置全局日期格式化规则，代码如下

```java
public class Jackson {	
	@Test
	public void setDateFormatTest() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<>(1);
		map.put("date", new Date());
		/*
		输出
			{"date":1538829478559}
		说明
			未指定日期格式化规则，输出时间戳
		 */
		System.out.println(objectMapper.writeValueAsString(map));
		/*
		输出
			{"date":"2018-10-06 20:37:58"}
		说明
			指定日期格式化规则后，输出指定日期格式
		 */
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		System.out.println(objectMapper.writeValueAsString(map));
	}
}
```

#### 设置某日期属性格式化规则

通过`@JsonFormat`设置某日期属性的格式化规则，代码如下

```java
public class Jackson {
	@Data
	public class JsonFormatClass {
		private Date date1 = new Date();
		// 指定日期格式化规则
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date date2 = new Date();
	}

	@Test
	public void jsonFormatTest() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		/*
		输出
			{"date1":1538830016475,"date2":"2018-10-06 12:46:56"}
		说明
			只有date2指定了日期格式化规则，因此只有date2输出指定日期格式
		 */
		System.out.println(objectMapper.writeValueAsString(new JsonFormatClass()));
	}
}
```

#### @JsonAlias

在反序列化时，指定Java属性可以接收多个名字

与@JsonProperty的区别是，@JsonProperty在序列化和反序列化时都生效