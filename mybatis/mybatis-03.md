[TOC]

# Mapper配置详解

本文介绍Mapper配置

## 基于XML的Mapper配置

### 增、删、改、查

增删改查SQL配置具有一定的相似性

```xml
<mapper>
	<select 
		id="selectPerson"
		parameterType="int"
		parameterMap="deprecated"
		resultType="hashmap"
		resultMap="personResultMap"
		flushCache="false"
		useCache="true"
		timeout="10000"
		fetchSize="256"
		statementType="PREPARED"
		resultSetType="FORWARD_ONLY">
		sql
	</select>	
	<insert
		id="insertAuthor"
		parameterType="domain.blog.Author"
		flushCache="true"
		statementType="PREPARED"
		keyProperty=""
		keyColumn=""
		useGeneratedKeys=""
		timeout="20">
		sql
	</insert>
	<update
		id="updateAuthor"
		parameterType="domain.blog.Author"
		flushCache="true"
		statementType="PREPARED"
		timeout="20">
		sql
	</update>
	<delete
		id="deleteAuthor"
		parameterType="domain.blog.Author"
		flushCache="true"
		statementType="PREPARED"
		timeout="20">
		sql
	</delete>
</mapper>
```
### 查询

#### 查询结果返回Map

1 配置resultType属性为HashMap

```xml
<select id="findById" resultType="java.util.HashMap">
	select * from User where id = #{id}
</select>
```

2 查询

```java
// 每列列名大小写都会作为key储存
// 例如：id可以通过map.get("id")或map.get("ID")获得
Map map = (Map)session.selectOne("xxxx.findById",1L);
```

### 缓存

	一级缓存基于session
	条件相同的两次查询，只执行一次sql，第一次执行的结果会缓存下来
	若中间执行了删改增操作，会清空缓存
	若中间关闭了session，会清空缓存
	二级缓存基于mapper

## 基于注解的Mapper配置

### 增、删、改、查

```java
public interface UserMapper {
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
```

@Insert、@Delete、@Update、@Select的value属性都是数组，经过测试，会将字符串连接起来一次执行，也就是如果是多个SQL，每个SQL需要以`;`结束。

对于@Select，会将第一条SQL的结果返回，其余SQL也会执行，但不会返回结果。

## 方法重载

Mapper必须保证唯一，但是方法可以重载，具体如下。

```java
public interface UserMapper{
    @Select("select * from user where id = #{id}")
	public User selectUser(User user);
	public User selectUser(Map<String,Object> user);
	public User selectUser(@Param("id") int id);
}
```