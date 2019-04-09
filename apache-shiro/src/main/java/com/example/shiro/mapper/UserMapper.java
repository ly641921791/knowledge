package com.example.shiro.mapper;

import com.example.shiro.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ly
 */
@Mapper
public interface UserMapper {

	@Select({
			"SELECT u.*, r.*, p.* FROM user u ",
			"INNER JOIN user_role ur on ur.uid = u.uid ",
			"INNER JOIN role r on r.rid = ur.rid ",
			"INNER JOIN role_permission pr on pr.rid = r.rid ",
			"INNER JOIN permission p on pr.pid = p.pid "})
	@ResultMap("userMap")
	List<User> findAll();

	@Select({
			"SELECT u.*, r.*, p.* FROM user u ",
			"INNER JOIN user_role ur on ur.uid = u.uid ",
			"INNER JOIN role r on r.rid = ur.rid ",
			"INNER JOIN role_permission pr on pr.rid = r.rid ",
			"INNER JOIN permission p on pr.pid = p.pid ",
			"WHERE u.username = #{username}"})
	@ResultMap("userMap")
	User findByUsername(String username);

//	@Select({
//			"SELECT * FROM role r ",
//			"INNER JOIN user_role ur on ur.uid = r.rid ",
//			"WHERE uid = #{uid}"
//	})
//	Set<Role> findByUid(Integer uid);
//
//	@Select("SELECT * FROM user WHERE username = #{username}")
//	@Results({
//			@Result(property = "uid", column = "uid", jdbcType = JdbcType.INTEGER, id = true),
//			@Result(property = "username", column = "username", jdbcType = JdbcType.VARCHAR),
//			@Result(property = "password", column = "password", jdbcType = JdbcType.VARCHAR),
//			@Result(property = "roles", column = "uid", many = @Many(select ="com.example.shiro.mapper.UserMapper.findByUid"))
//	})
//	User findByUsername(String username);

}
