[TOC]

# Spring Boot Validation

## 摘要

　　服务端数据校验属于必不可少又与业务无关的操作，JSR303/JSR349规定了注解校验规范，通过注解实现对参数的校验，实现了校验操作与业务代码的分离。

## 使用

### 准备工作

　　准备启动类、被校验实体类

```java
/**
* 启动类
*/
@SpringBootApplication
public class ValidationApplication {
	public static void main(String[] args) {
		SpringApplication.run(ValidationApplication.class, args);
	}
}
/**
* 被校验实体类
*/
@Data
public class ValidationParam {
	@NotBlank
	private String name;
}
```

### 使用说明

　　将@Valid和@Validated标记在类上可以对每个方法每个参数开启参数校验，还可以标记在方法或参数上实现细粒度校验。区别是Spring提供了更多校验注解，@Validated对这些注解都有支持，@Valid不支持（网上看到的，并未实践）。

#### 测试@Validation是否生效

```
// @RequestMapping
@RequestMapping("/validation1")
public Object validation1(@Validated ValidationParam param) {
	return param;
}

// 访问http://localhost:8080/validation1得到下面结果
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Thu Sep 27 23:48:29 CST 2018
There was an unexpected error (type=Bad Request, status=400).
Validation failed for object='validationParam'. Error count: 1
```

#### 校验数据，并获取校验结果

　　可以通过CheckBean1、BindingResult1、CheckBean2、BindingResult2...的顺序获取每个Bean的参数校验结果，对结果进行处理。

　　若没有对BindingResult进行获取，会抛出异常，此时可以通过捕获全局异常处理绑定结果。

```
// @RequestMapping
@RequestMapping("/validation2")
public Object validation2(@Validated ValidationParam param, BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
		return bindingResult.getFieldErrors();
	}
	return param;
}

// 访问http://localhost:8080/validation2得到下面结果
[{"codes":["NotBlank.validationParam.name","NotBlank.name","NotBlank.java.lang.String","NotBlank"],"arguments":[{"codes":["validationParam.name","name"],"arguments":null,"defaultMessage":"name","code":"name"}],"defaultMessage":"不能为空","objectName":"validationParam","field":"name","rejectedValue":null,"bindingFailure":false,"code":"NotBlank"}]
```

#### 自定义异常信息

https://blog.csdn.net/catoop/article/details/51278675
https://blog.csdn.net/u013815546/article/details/77248003/

#### 分组校验

#### 自定义校验

#### 手动校验