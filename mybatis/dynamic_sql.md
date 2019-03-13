MyBatis SQL
-

本文介绍MyBatis动态SQL的使用，MyBatis提供了XML和注解两种实现方式

### XML实现动态SQL

XML实现动态SQL是基于OGNL表达式实现的，MyBatis提供了以下标签：

|标签|说明|
|---|---|
|if|类似Java的if语句|
|choose|类似Java的switch语句|
|where、set、trim|用于生成复杂SQL|
|foreach|用于处理集合类型参数|
|bind|绑定参数到OGNL表达式|

#### if

当条件满足时，插入标签内SQL

例如：当传入title时，查询指定title博客，否则查询全部

```xml
<select>
	SELECT * FROM blog
	<if test="title != null">
		WHERE title = #{title}
	</if>
</select>
```

#### choose

常与when、otherwise一起使用，当条件满足时，插入标签内SQL，并结束当前choose标签

例如：当type=1时，按title查询，当type=2时，按content查询，否则按author查询

```xml
<select>
	SELECT * FROM blog
	<choose>
		<when test="type == 1">WHERE title = #{param}</when>
		<when test="type == 2">WHERE content = #{param}</when>
		<otherwise>WHERE author = #{param}</otherwise>
	</choose>
</select>
```

#### where

用于生成WHERE语句，内部嵌套其他标签，只有当内部的标签有SQL返回时，才会生效插入WHERE条件，并且会删除多余的AND、OR关键字

例如：将非NULL参数，加入WHERE条件，下面的语句，即使第一个if不生效，也可以生成正确的SQL

```xml
<select>
	SELECT * FROM blog
	<where>
		<if test="title != null">
			title = #{title}
		</if>
		<if test="content != null">
			AND content = #{content}
		</if>
		<if test="author != null">
			AND author = #{author}
		</if>
	</where>
</select>
```

#### set

用于生成SET语句，类似where标签，会删除多余的逗号

例如：将非NULL参数更新，下面的语句，即使最后一个if不生效，也可以生成正确的SQL

```xml
<update>
	UPDATE blog
	<set>
		<if test="title != null">
			title = #{title},
		</if>
		<if test="content != null">
			content = #{content},
		</if>
		<if test="author != null">
			author = #{author}
		</if>
	</set>
	WHERE id = #{id}
</update>
```

#### trim

通过一定规则处理标签内容，有以下属性：

- prefix ：内容增加指定前缀
- suffix ：内容增加指定后缀
- prefixOverrides ：内容忽视指定前缀
- suffixOverrides ：内容忽视指定后缀

通过trim可以实现where和set标签，如下

```xml
<trim prefix="where" prefixOverrides="AND | OR">

</trim>
```

```xml
<trim prefix="set" suffixOverrides=",">

</trim>
```

#### foreach

用于处理集合类参数，有以下属性：

- index ：当前遍历的下标
- item ：当前遍历的项
- collection ：被遍历的参数
- separator ：两项间的分隔符
- open ：最终内容的前缀
- close ：最终内容的后缀

例如：查询指定id的blog

```xml
<select>
	SELECT * FROM blog WHERE id IN 
	<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
		#{item}
	</foreach>
</select>
```

#### bind

绑定参数到OGNL表达式

例如：模糊查询

```xml
<select>
	<bind name="titleLike" value="'%' + title + '%'"/>
	SELECT * FROM blog WHERE title LIKE #{titleLike}
</select>
```

### 注解实现动态SQL

注解实现动态SQL是基于反射实现的，MyBatis提供了以下注解：

- @InsertProvider
- @DeleteProvider
- @UpdateProvider
- @SelectProvider

四个注解使用方法类似，以@SelectProvider为例

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SelectProvider {
  Class<?> type();
  String method();
}
```

通过type指定提供SQL的方法所在类，通过method指定提供SQL的方法名

例如：

```java
public interface BlogMapper {
    @SelectProvider(type=SqlProvider.class,method="findById")
    Blog findById(long id);
    
    class SqlProvider {
        public String findById(){
            return "SELECT * FROM blog WHERE id = #{id}";
        }
    }
}
```

#### 参数传递

Mapper方法单参数，可以直接传入，如：

```java
public interface BlogMapper {
    @SelectProvider(type=SqlProvider.class,method="findById")
    Blog findById(long id);
    
    class SqlProvider {
        public String findById(long id){
            return "SELECT * FROM blog WHERE id = #{id}";
        }
    }
}
```

Mapper方法多参数，可以通过Map传入，如：

```java
public interface BlogMapper {
    @SelectProvider(type=SqlProvider.class,method="findById")
    Blog findById(@Param("id1")long id1,@Param("id2")long id2);
    
    class SqlProvider {
        public String findById(Map<String,Object> param){
            long id1 = (long) param.get("id1");
            long id1 = (long) param.get("id2");
            return "SELECT * FROM blog WHERE id IN (#{id1},#{id2})";
        }
    }
}
```

Mapper方法多参数，还可以通过@Param指定传入参数，如：

```java
public interface BlogMapper {
    @SelectProvider(type=SqlProvider.class,method="findById")
    Blog findById(@Param("id1")long id1,@Param("id2")long id2);
    
    class SqlProvider {
        public String findById(@Param("id1")long id1,@Param("id2")long id2){
            return "SELECT * FROM blog WHERE id IN (#{id1},#{id2})";
        }
    }
}
```