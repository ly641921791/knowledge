package com.example.shiro.service.impl;

import com.example.shiro.bean.Permission;
import com.example.shiro.bean.Role;
import com.example.shiro.bean.User;
import com.example.shiro.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	/**
	 * 授权使用（认证登录后授权）
	 *
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		User user = (User) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
		List<String> permissionList = new ArrayList<>();
		Set<Role> roles = user.getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			roles.forEach(role -> {
				Set<Permission> permissions = role.getPermissions();
				if (CollectionUtils.isNotEmpty(permissions)) {
					permissions.forEach(permission -> permissionList.add(permission.getName()));
				}
			});
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissionList);
		return info;
	}

	/**
	 * 认证登录使用
	 *
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		User user = userService.findByUsername(token.getUsername());
		return new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());
	}
}
