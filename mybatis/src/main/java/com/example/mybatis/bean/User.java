package com.example.mybatis.bean;

import com.example.mybatis.enums.SexEnum;
import lombok.Data;

/**
 * @author ly
 */
@Data
public class User {

    private Integer id;

    private String name;

    private SexEnum sex;

}
