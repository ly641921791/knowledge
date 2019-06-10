# MyBatis教程

简介

MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Old Java Objects,普通的 Java对象)映射成数据库中的记录。

本文

本文是MyBatis教程的目录，参考过的书籍、文档、博客都会在最后列出来，不定时更新，随时太监。

目录

- [教程-简单入门](mybatis-01.md)
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



