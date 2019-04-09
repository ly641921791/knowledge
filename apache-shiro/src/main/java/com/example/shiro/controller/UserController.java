package com.example.shiro.controller;

import com.example.shiro.bean.User;
import com.example.shiro.mapper.UserMapper;
import com.example.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;

	@GetMapping
	public List<User> user() {
		//return userService.findByUsername("admin");
		return userMapper.findAll();
	}
}
