MyBatis
-

MyBatis 是一个基于Java的持久层框架

依赖坐标

``` xml
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.2</version>
</dependency>
```

使用流程

1. 创建 SqlSessionFactoryBuilder
2. 通过 SqlSessionFactoryBuilder 解析配置得到 SqlSessionFactory（build方法）
3. 通过 SqlSessionFactory 获得一个 SqlSession （openSession方法）
4. 通过 SqlSession 执行SQL，SqlSession 中包含一个可用的数据库连接，使用完需要归还连接
5. 关闭 SqlSession（close方法）

相关目录

- [简单示例](example.md)

- [教程-Config配置详解](mybatis-02.md)
- [教程-Mapper配置详解](mybatis-03.md)
- [教程-各种情况使用示例](mybatis-04.md)

- [源码-初始化](mybatis-11.md)

相关参考

> 官网文档 ：http://www.mybatis.org/mybatis-3/zh/index.html



###### 核心组件

- SqlSession
- Executor
- StatementHandler
- ParameterHandler
- ResultSetHandler
- TypeHandler
- MappedStatement
- Configuration

###### 执行步骤

1. 加载配置文件得到XMLConfigBuilder
2. parse()得到Configuration
3. build()得到SqlSessionFactory
4. openSession()得到SqlSession
5. 将SQL执行委派给Executor
6. 生成StatementHandler
7. StatementHandler通过ParameterHandler得到Statement
8. execute()得到ResultSet
9. 通过ResultSetHandler得到最终结果

###### 插件

https://mp.weixin.qq.com/s/BPVq7ajj2rKgNkwUL3jxGQ
