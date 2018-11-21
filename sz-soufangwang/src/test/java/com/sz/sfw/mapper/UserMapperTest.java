package com.sz.sfw.mapper;

import com.sz.sfw.SfwApplicationTest;
import com.sz.sfw.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperTest extends SfwApplicationTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindOne() {
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

}