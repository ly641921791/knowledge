package com.example.mybatis.controller;

import com.example.mybatis.bean.User;
import com.example.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ly
 */
@RestController
public class TypeHandlerController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/addUser")
    public User addUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userMapper.findAll();
    }

}
