package com.example.shiro.service;

import com.example.shiro.bean.User;

/**
 * @author ly
 */
public interface UserService {

	User findByUsername(String username);

}
