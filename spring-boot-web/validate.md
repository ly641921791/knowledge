[TOC]

# Spring Boot Validate

　　Spring Boot 支持JSR303/JSR349验证框架，通过注解实现对方法参数的校验，并将校验结果放在BindingResult对象中。

## 常用校验注解

　　下面的表格列出常用校验类及主要功能。

|检查类型|注解|说明|
|---|---|---|
|空检查|@Null|验证对象为null|
| |@NotNull|验证对象不为null|
| |@NotEmpty|验证对象不为null、且长度>0|
| |@NotBlank|验证字符串不为null、且最少有一个非空格字符|
|长度检查|@Size(min,max)|验证对象长度|
| |@Length(min,max)|验证字符串长度|
|数值检查|@Min(value)|验证数字>=value|
| |@Max(value)|验证数字<=value|
| |@Digits(integer,fraction)|验证数字格式|
| |@Range(min,max)|验证数字是否符合[min,max]|
|正则检查|@Pattern(regexp)|验证字符串是否符合正则表达式|
| |@Email|验证是否邮箱格式|

　　注 ：对于长度的校验基本都支持字符串、集合、Map、数组的长度。

　　有两个注解比较特殊 ：@Valid和@Validated。这两个注解必须配合其他注解使用。有时候方法的多个参数会被封装成JavaBean，每个属性都拥有不用的校验，通过这两个注解可以开启JavaBean属性的校验。下面是两个注解的不同之处。

| |@Valid|@Validated|
|---|---|---|
|分组校验|不支持|支持|
|使用范围|方法、属性、构造方法、参数|类、方法、参数|
|嵌套验证|支持|不支持|

　　注 ：嵌套验证。JavaBean a中某个属性是另一个JavaBean b，对a进行验证的同时验证b。

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