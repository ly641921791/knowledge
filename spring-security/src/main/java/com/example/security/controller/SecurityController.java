package com.example.security.controller;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ly
 */
@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityController {

	/**
	 * 配置该请求路径无需验证
	 */
	@RequestMapping("/")
	public String index() {
		return "Security Application";
	}

	/**
	 * 任何验证都可以通过
	 */
	@RequestMapping("/authWithAnybody")
	public String authWithAnybody() {
		return "authWithAnybody";
	}

	/**
	 * USER角色才可以验证通过
	 * <p>
	 * 下面四个注解需要配合 @EnableGlobalMethodSecurity(prePostEnabled = true) 使用
	 * <p>
	 * 在请求后验证权限
	 * {@link PreAuthorize}
	 * {@link PostAuthorize}
	 * <p>
	 * 对集合参数或返回值进行过滤
	 * {@link PreFilter}
	 * {@link PostFilter}
	 * <p>
	 * 表达式支持的方法 {@link SecurityExpressionRoot}
	 * <p>
	 * 支持 and or 还有参数的验证
	 */
	@RequestMapping("authWithUser")
	// 有USER权限 且 参数和当前用户账号相同
	@PreAuthorize("hasRole('ROLE_USER') and principal.username.equals(#username)")
	// 返回结果是 authWithUser
	@PostAuthorize("returnObject.equals('authWithUser')")
	public String authWithUser(String username) {
		return "authWithUser";
	}


	@PreFilter("filterObject.username=='admin'")
	@PostFilter("filterObject.username=='admin'")
	public List<User> filter(List<User> users) {
		return null;
	}
}
