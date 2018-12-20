package com.sz.seckill.service;

import com.sz.seckill.mapper.UserMapper;
import com.sz.seckill.to.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ly
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(long id) {
        return userMapper.findById(id);
    }

}
