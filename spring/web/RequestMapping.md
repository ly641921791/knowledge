RequestMapping
-

value：请求路径，支持占位符
	value="/{id}" 此时id为占位符，可以作为参数使用，通过@PathVariable提取


params：请求参数列表，并支持表达式
	params="!id" 参数不能有id
	params="name!=ly" 参数中name不能等于ly

##### path

请求路径

###### 功能1 通配符

- `?` 匹配1个字符
- `*` 匹配至少0个字符
- `**` 匹配至少0个路径段	

###### 功能2 URI参数

通过@PathVariable获得参数，如下

``` java
@RequestMapping("/{param1}")
public Class Demo {
	@RequestMapping("/{param2}")
	public String demo(@PathVariable String param1, @PathVariable String param2) {
		// ...
	}
}
```

###### 功能3 正则表达式URI参数

支持URI参数功能，语法{varName:regex}

``` java
@GetMapping("/{name:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{ext:\\.[a-z]+}")
public void handle(@PathVariable String version, @PathVariable String ext) {
    // ...
}
```

###### 功能4 ${}占位符



##### consumes

处理的请求类型

##### produces

返回的响应类型


> 官网文档 https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/web.html#mvc-ann-requestmapping



通过@RequestParam注解接受List

@RequestParam("idList[]") List<String> idList

表单将参数通过 arg[0] arg[1] ... 传递，可以通过String[] arg接收

配置返回状态码

@ResponseStatus

