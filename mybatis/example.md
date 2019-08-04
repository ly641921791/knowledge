简单示例
-

###### 引入依赖

``` xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
```

###### 配置文件

BlogMapper.xml 文件，用于配置SQL语句

``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.mybatis.example.BlogMapper">
  <select id="selectBlog" resultType="Blog">
    select * from Blog where id = #{id}
  </select>
</mapper>
```

mybatis-config.xml 文件，用于配置数据源等信息

``` xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
      </dataSource>
    </environment>
  </environments>
  <mappers>
    <mapper resource="org/mybatis/example/BlogMapper.xml"/>
  </mappers>
</configuration>
```

###### 执行SQL

```java
public class Example {
    public static void main(String[] args){
        // 读取配置
        InputStream configuration = Resources.getResourceAsStream("org/mybatis/example/mybatis-config.xml");
        
    	// 创建 SqlSessionFactoryBuilder
    	SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    	
    	// 创建 SqlSessionFactory
    	SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);
    	
    	// 获得 SqlSession
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	
    	// 执行SQL
    	Blog blog = (Blog) sqlSession.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);
    	
    	// 关闭 SqlSession
    	sqlSession.close();
    }
}
```
