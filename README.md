[TOC]

# knowledge


## java

### java基础

- [序列化](./java/serialize.md)

## 框架

- [MyBatis教程](./mybatis/mybatis.md)


#### JDK1.5-1.8特性总结

#### JDK1.5

- 自动装箱与拆箱
- 枚举
- 静态导入
- 可变参数
- 内省（Introspector）
- 泛型
- For-Each循环

#### JDK1.6

- 

#### JDK1.7

#### JDK1.8

- 接口默认方法



## 事务

- [分布式事物](./transaction/distributed%20transaction.md)


## spring boot

### spring boot mvc

- [JSON](./spring-boot-mvc-json/json.md)
- [参数校验](./spring-boot-mvc-validate/validate.md)

#### 

- [Spring Boot MyBatis]()

### 源码

- [Spring Boot 启动过程（一）SpringBootApplication](./spring-boot-source-code/SpringBootApplication.md)
- [Spring Boot 启动过程（二）SpringApplication](./spring-boot-source-code/SpringApplication.md)
- [Spring Boot AOP原理](./spring-boot-source-code/AOP.md)














### 领域模型规约

阿里规约（题主只是用VO和DO）

|简写|全写|说明|
|---|---|---|
|DO|Data Object|数据库表映射类，DAO层向上传输数据使用|
|DTO|Data Transfer Object|数据传输对象，Service层向上传输数据使用|
|BO|Business Object|业务对象，Service输出的封装业务逻辑对象|
|AO|Application Object|应用对象，Web层和Service层之间抽象的复用对象|
|VO|View Object|显示层对象，Web向模板传输的对象|
|Query|数据查询对象|各层接收上层的查询请求|

个人使用

|简写|全写|说明|
|---|---|---|
|DO|Data Object|数据库表映射类，DAO层向上传输数据使用|
|PO|Param Object|参数类，封装参数|
|VO|View Object|显示层对象，Web向模板传输的对象|



### QPS预估

如何评估并发量（其他数据评估方式类似）

1 评估总访问量

从运营处和产品处得到预期访问

例如：推送活动，30分钟完成5000W用户的推送，预计点击率10%，则总访问量为500W

2 评估平均QPS

总量除以总时间

例如：推送活动的30分钟内，QPS=500W/(30*60)=2778 
例如：日均访问量8000W，白天大约4W秒，QPS=8000W/4W=2000

3 评估高峰QPS

需要根据业务曲线评估

假设：峰值是均值的2.5倍，峰值QPS=2000*2.5=5000

4 评估系统单机极限QPS

通过压测得到

Tomcat单机QPS=1200
数据库QPS=500

5 根据高峰QPS、单机QPS预估需要的

## Nginx教程

### 安装

- [yum安装](./nginx/install-yum.md)
- [docker安装](./nginx/install-docker.md)
