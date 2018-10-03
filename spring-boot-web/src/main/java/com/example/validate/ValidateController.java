package com.example.validate;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.HashSet;
import java.util.Set;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@RestController
@RequestMapping("/validate")
public class ValidateController {

	@Autowired
	private Validator validator;

	@RequestMapping("/addUser")
	public Integer addUser(@Validated(User.Add.class) User user) {
		// 假设插入数据库后，生成id为89757
		user.setId(89757);
		// 插入成功后返回id
		return user.getId();
	}

	@RequestMapping("/updateUser")
	public boolean updateUser(@Validated(User.Update.class) User user) {
		// 假设修改成功
		return true;
	}

	@RequestMapping("/handlerBindingResult")
	public Object bindingResult(@Validated(User.Add.class) User user, BindingResult bindingResult) {
		// 若存在校验异常则处理
		if (bindingResult.hasErrors()) {
			StringBuilder errorMessage = new StringBuilder();
			// 遍历全部校验异常并拼接异常信息
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMessage.append(error.getField()).append(error.getDefaultMessage()).append('\n');
			}
			return errorMessage.toString();
		}
		return true;
	}

	@RequestMapping("/activeValidate")
	public String activeValidate(User user) {
		Set<ConstraintViolation<User>> validate = validator.validate(user, User.Add.class);
		StringBuilder result = new StringBuilder();
		for (ConstraintViolation<User> violation : validate) {
			result.append(violation.getPropertyPath()).append(violation.getMessage());
		}
		return result.toString();
	}
}

@Data
class User {
	// 定义新增校验组类
	public interface Add {}

	// 定义更新校验组类
	public interface Update {}

	// 添加校验并指定不同的校验组
	@Null(groups = Add.class)
	@NotNull(groups = Update.class)
	private Integer id;

	// 添加新增和修改校验组
	@NotBlank(groups = {Add.class, Update.class})
	private String name;

	@Sex(allowed = {0, 1, 2}, groups = {Add.class, Update.class})
	private Integer sex;
}

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = SexValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@interface Sex {
	String message() default "{com.example.validate.sex}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int[] allowed() default {1, 2};
}

class SexValidator implements ConstraintValidator<Sex, Integer> {

	private Set<Integer> allowed = new HashSet<>(2);

	@Override
	public void initialize(Sex sex) {
		for (int i : sex.allowed()) {
			allowed.add(i);
		}
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		return allowed.contains(value);
	}
}