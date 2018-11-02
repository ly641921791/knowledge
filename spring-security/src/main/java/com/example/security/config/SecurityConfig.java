package com.example.security.config;

import com.example.security.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * @author ly
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserServiceImpl userService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// 配置基于内存的验证。视频中与数据库验证冲突
		auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("ly").password("123456").roles("USER");

		// 基于数据库的配置。表结构在 users.ddl 已经给出。

		// 配置基于数据库的验证。视频中与内存验证冲突
		auth.userDetailsService(userService)
				.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

		// 配置基于数据库的验证。
		auth.jdbcAuthentication()
				.usersByUsernameQuery("")
				.authoritiesByUsernameQuery("")
				.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// 允许通过的请求
				.antMatchers("/").permitAll()
				// 其他请求需要验证
				.anyRequest().authenticated()
				.and()
				.logout().permitAll()
				.and()
				.formLogin();
		http.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/css/**", "/img/**");
	}
}
