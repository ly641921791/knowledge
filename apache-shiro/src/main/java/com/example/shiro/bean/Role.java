package com.example.shiro.bean;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ly
 */
@Data
public class Role {

	private Integer rid;
	private String name;
	private Set<Permission> permissions = new HashSet<>();
	private Set<User> users = new HashSet<>();

}
