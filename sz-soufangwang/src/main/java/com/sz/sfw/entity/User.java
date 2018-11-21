package com.sz.sfw.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author ly
 */
@Data
public class User {

    private Long id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private int status;

    private Date createTime;

    private Date lastLoginTime;

    private Date lastUpdateTime;

    /**
     * 头像
     */
    private String avatar;
}
