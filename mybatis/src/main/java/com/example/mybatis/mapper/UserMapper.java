package com.example.mybatis.mapper;

import com.example.mybatis.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ly
 */
@Mapper
public interface UserMapper {

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO user (name,sex) VALUES (#{name},#{sex})")
    void insert(User user);

    @Select("SELECT * FROM user")
    List<User> findAll();

}
