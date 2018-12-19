[TOC]

# 简单入门

本文提供一个入门示例

## 引入JAR包

这里通过maven管理依赖

```xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.6</version>
</dependency>
```

## 初识核心类

MyBatis使用从下面三个核心类开始的

- SqlSessionFactoryBuilder ：通过提供的`build`方法可以解析配置，构建`SqlSessionFactory`对象
- SqlSessionFactory ：通过提供的`openSession`方法，获取一个用于执行SQL的`SqlSession`
- SqlSession ：执行SQL的核心对象，使用完需要关闭，可以利用try语句实现自动关闭。

## 初探核心类

**准备配置文件**

准备两个配置文件，userMapper.xml用于配置SQL，表示User表相关SQL的配置文件，MyBatis-config.xml是配置数据源、SQL文件的配置文件

userMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.mybatis.example.UserMapper">
    <select id="findById" resultType="org.mybatis.example.User">
        select * from User where id = #{id}
    </select>
</mapper>
```

MyBatis-config.xml

```xml
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

**读取配置并执行SQL**

```java
public class Demo {
    public static void main(String[] args) {
        // 创建SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = builder.build(Resources.getResourceAsStream("config.xml"));

        // 创建SqlSession并执行配置的SQL
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User user = (User) sqlSession.selectOne("org.mybatis.example.UserMapper.findById", 1L);
            System.out.println(user);
        }
    }
}
```

## 详解核心类

### SqlSessionFactoryBuilder

SqlSessionFactoryBuilder的作用只有一个，解析配置构建SqlSessionFactory对象，使用完就可以丢弃。

### SqlSessionFactory

SqlSessionFactory的作用是创建一个可用的SqlSession，与应用共生死。

### SqlSession

SqlSession使用完成需及时关闭释放资源。

示例中的用法比较容易写错，因此MyBatis还提供了下面的用法

```java
try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    // UserMapper的包名必须和UserMapper.xml的命名空间相同
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.findById(1L);
    System.out.println(user);
}
```