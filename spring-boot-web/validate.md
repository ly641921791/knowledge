[TOC]

# Spring Boot Validate

　　Spring Boot 支持JSR303/JSR349验证框架，通过注解实现对参数的校验，并将校验结果封装成BindingResult对象。

## 常用注解

　　下面的表格列出常用校验类及主要功能。这些注解必须配合@Valid或@Validated使用，通过这两个注解开启校验。

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



　　下面是@Valid和@Validated的区别。

| |@Valid|@Validated|
|---|---|---|
|分组校验|不支持|支持|
|使用范围|方法、属性、构造方法、参数|类、方法、参数|
|嵌套验证|支持|不支持|

　　注 ：嵌套验证。JavaBean a中某个属性类型是JavaBean b，对a进行验证的同时验证b。



## 使用说明

### 基本使用

　　以新增用户为例

**新建User类，为name属性添加@NotBlank注解**

```
@Data
class User {
	private Integer id;
	@NotBlank
	private String name;
}
```

**新增新建用户接口，添加@Validated注解开启user参数校验**

```
@RestController
@RequestMapping("/validate")
public class ValidateController {
	@RequestMapping("/addUser")
	public Integer addUser(@Validated User user) {
		// 假设插入数据库后，生成id为89757
		user.setId(89757);
		// 插入成功后返回id
		return user.getId();
	}
}
```

**启动服务，测试代码**

```
浏览器输入 ：http://localhost:8080/validate/addUser
由于name是null，校验不通过，返回错误

浏览器输入 ：http://localhost:8080/validate/addUser?name=ly
校验通过，返回89757
```

### 实现分组校验

　　同一个JavaBean在不同的场景可能需要不用的校验规则，例如：添加用户时id必须是null，修改用户时id不能是null。每一个校验注解都有group属性，通过group属性可以实现不同方法采用不同的校验规则。以新建用户为例

**修改User类，添加并指定校验组**

```
@Data
class User {
	// 定义新增校验组和更新校验组
	public interface Add {}
	public interface Update {}

	// 添加校验并指定不同的校验组
	@Null(groups = Add.class)
	@NotNull(groups = Update.class)
	private Integer id;

	// 添加新增和修改校验组
	@NotBlank(groups = {Add.class, Update.class})
	private String name;
}
```

**添加修改用户接口，为新增和修改用户接口指定校验组**

```
@RestController
@RequestMapping("/validate")
public class ValidateController {
	// 通过group属性指定新增用户校验组
	@RequestMapping("/addUser")
	public Integer addUser(@Validated(User.Add.class) User user) {
		// 同上，略
	}

	// 通过group属性执行修改用户校验组
	@RequestMapping("/updateUser")
	public boolean updateUser(@Validated(User.Update.class) User user) {
		// 假设修改成功，返回true
		return true;
	}
}
```

**启动服务，测试代码**

```
浏览器输入 ：http://localhost:8080/validate/addUser?id=1&name=ly
由于id不是空，校验不通过，返回错误

浏览器输入 ：http://localhost:8080/validate/addUser?name=ly
校验通过，返回89757

浏览器输入 ：http://localhost:8080/validate/updateUser?name=ly
由于id是空，校验不通过，返回错误

浏览器输入 ：http://localhost:8080/validate/updateUser?id=1&name=ly
校验通过，返回true
```

### 处理校验结果

　　Spring Boot 会将校验结果封装成BindingResult对象，可以通过BindingResult参数获取校验结果。

**新增方法，用于测试校验结果的获取**

```
@RestController
@RequestMapping("/validate")
public class ValidateController {
	
	// 其他方法略
	
	@RequestMapping("/handlerBindingResult")
	public Object bindingResult(@Validated(User.Add.class) User user, BindingResult bindingResult) {
		// 若存在校验异常则处理
		if (bindingResult.hasErrors()) {
			StringBuffer errorMessage = new StringBuffer();
			// 遍历全部校验异常并拼接异常信息
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMessage.append(error.getField()).append(error.getDefaultMessage()).append('\n');
			}
			return errorMessage.toString();
		}
		return true;
	}
}
```

**启动服务，测试代码**

```
浏览器输入 ：http://localhost:8080/validate/handlerBindingResult?id=1
返回处理后的错误信息 ：id必须为null name不能为空

浏览器输入 ：http://localhost:8080/validate/handlerBindingResult?name=ly
校验通过，返回true
```

　　注 ：若方法需要校验多个参数，例如：method(param1,param2...)，需要依次获取校验结果，例如 ：method(param1,bindingResult1,param2,bindingResult2...)。对于未通过参数注入处理的BindingResult对象，会抛出异常，可以通过捕获`MethodArgumentNotValidException`和`BindException`统一处理，个人习惯在这里处理。

### 自定义校验注解

　　当现有的校验注解不满足业务要求的时候，就需要通过自定义注解来满足我们自定义的校验规则。以性别校验为例，假设1代表男、2代表女，输入其他都是不合法的，下面说明如何自定义性别校验注解

**新建Sex注解，用于性别参数校验**

```
@Documented
@Retention(RUNTIME)
// 指定校验类
@Constraint(validatedBy = SexValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@interface Sex {
	// 必须的三个属性
	String message() default "参数不合法";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	// 允许值(默认1男2女)。若使用方的性别枚举不同，可以通过该字段指定校验通过的值。
	int[] allowed() default {1, 2};
}
```

**新增Sex注解校验类**

```
// 校验类需要实现ConstraintValidator接口，通过泛型指定被校验注解类型和被校验字段类型
class SexValidator implements ConstraintValidator<Sex, Integer> {
	private Set<Integer> allowed = new HashSet<>(2);

	// 初始化允许校验通过的值
	@Override
	public void initialize(Sex sex) {
		for (int i : sex.allowed()) {
			allowed.add(i);
		}
	}
	// 执行校验
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return allowed.contains(value);
	}
}
```

**修改User类**

```
@Data
class User {

	// 略
	
	// 假设注解使用方的性别枚举分别是0未知、1男、2女
	@Sex(allowed = {0, 1, 2}, groups = {Add.class, Update.class})
	private Integer sex;
}
```

**启动服务，测试代码**

```
浏览器输入 ：http://localhost:8080/validate/handlerBindingResult?name=ly&sex=4
返回处理后的错误信息 ：sex参数不合法

浏览器输入 ：http://localhost:8080/validate/handlerBindingResult?name=ly&sex=1
校验通过，返回true
```







#### 自定义异常信息

https://blog.csdn.net/catoop/article/details/51278675
https://blog.csdn.net/u013815546/article/details/77248003/

#### 手动校验