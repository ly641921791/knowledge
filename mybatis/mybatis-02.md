[TOC]

# MyBatis配置




SqlSession使用

	准备功能
		<select id="selectUser" resultType="User">
			select * from user where username like concat('%', #{username}, '%') and id &lt; #{id}
		</select>

		public interface UserMapper{
			//方法名和sqlId相同
			public User selectUser(User user);
			public User selectUser(Map<String,Object> user);
			public User selectUser(@Param("username") String username,@Param("id") int id);
		}

	使用：三种重载的使用
		try{
			UserMapper mapper = session.getMapper(UserMapper.class);

			User user = mapper.selectUser(new User());

			Map<String,Object> user = new Map<String,Object>();
			user.put("username","");
			user.put("id","");
			User user = mapper.selectUser(user);

			User user = mapper.selectUser("",1);

		}finally{
			session.close();
		}

	sql不写在mapper中
	public interface UserMapperI {
		@Insert("insert into users(name, age) values(#{name}, #{age})")
		public int add(User user);

		@Delete("delete from users where id=#{id}")
		public int deleteById(int id);

		@Update("update users set name=#{name} where id=#{id}")
		public int update(User user);

		@Select("select * from users where id=#{id}")
		public User getById(int id);

		@Select("select * from users")
		public List<User> getAll();
	}




## 获取自增主键

	@Options(userGenerationKey=true,keyProperty="user.id")



## @Insert、@Delete、@Update、@Select value属性是数组

针对SQL数组，会将其拼接执行。

对于@Select，SQL拼接执行的同时，会将第一条SQL的结果返回。