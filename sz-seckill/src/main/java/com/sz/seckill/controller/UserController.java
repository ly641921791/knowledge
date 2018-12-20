package com.sz.seckill.controller;

import com.sz.seckill.result.ApiResult;
import com.sz.seckill.service.UserService;
import com.sz.seckill.to.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ly
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    @ResponseBody
    public ApiResult<User> findById(long id){
        return ApiResult.success(userService.findById(id));
    }

}
