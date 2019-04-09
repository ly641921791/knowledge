package com.example.mybatis.controller;

import com.example.mybatis.bean.User;
import com.example.mybatis.mapper.UserMapper;
import com.swift.custom.mapper.param.SelectCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/hello")
    public List<User> hello() {
        return userMapper.select(new SelectCondition().eq("id",1));
    }

}
