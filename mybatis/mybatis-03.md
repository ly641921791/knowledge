[TOC]

# Mapper配置

本文介绍Mapper文件配置

## 返回Map结果

```xml
    <select id="findById" resultType="org.mybatis.example.User">
        select * from User where id = #{id}
    </select>
```


返回Map类型结果
	<select id="" parameterType="int" resultType="java.util.HashMap">
		select id,name from emp where id = #{id}
	<select>

	Map map = (Map)session.selectOne("sqlId",10);
	map.get("id");
	map.get("name");
	注意：返回HashMap类型的结果，表格字段大小写都会作为key存储，如id字段，可以通过map.get("id")或map.get("ID")得到

	<select id="" resultType="java.util.HashMap">
		select id,name from emp
	</select>

	List<Map> list = session.selectList("sqlId");
	for(Map map : list){
	}






MyBatis缓存
		一级缓存基于session
		条件相同的两次查询，只执行一次sql，第一次执行的结果会缓存下来
		若中间执行了删改增操作，会清空缓存
		若中间关闭了session，会清空缓存
		二级缓存基于mapper
