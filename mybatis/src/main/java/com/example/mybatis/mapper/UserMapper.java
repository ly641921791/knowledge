package com.example.mybatis.mapper;

import com.example.mybatis.bean.User;
import com.swift.custom.swift.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ly
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

//    @Options(useGeneratedKeys = true)
//    @Insert("INSERT INTO user (name,sex) VALUES (#{name},#{sex})")
//    void insert(User user);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("${list[0]}")
    List<User> find(List list);

}

