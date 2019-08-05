[TOC]

# knowledge


## java

### java基础

- [序列化](./java/serialize.md)

## 框架

- [MyBatis教程](mybatis/README.md)




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


API接口规范

接口中捕获到的异常，响应状态码统一为200，有的框架/浏览器会对特殊响应码针对性处理，例如404重定向错误页，导致响应处理未按照指定逻辑走


## Docker

- [修改容器端口](docker/modify_the_container_port.md)

## Java

- [反射 & 泛型](./java/reflect%20&%20generics.md)
- [并发——锁](java/juc/lock.md)

## MQ

### RocketMQ

- [介绍](./mq/rocketmq/README.md)
- [安装](mq/rocketmq/install.md)
- [示例](./mq/rocketmq/example.md)

## MyBatis

- [动态SQL](./mybatis/dynamic_sql.md)

## 数据库

- [MySQL](mysql/README.md)

## Redis

- [分布式锁](./redis/distributed%20lock.md)

## Web Server

- [qps](website/qps.md)
- [过滤器和拦截器](https://mp.weixin.qq.com/s/c9d-avYSkhljLNDFVvFggA)





###### 代理服务器

- [Nginx](nginx/README.md)
- [LVS](lvs.md)

###### 分布式系统

- [分布式事物](distributed_system/transaction.md)



###### [负载均衡](http://www.sohu.com/a/233936157_262549)

- LVS
- Nginx
- HAProxy


面试题

ES脑裂问题分析及优化

Redis常用数据结构和操作

dubbo原理

JUC，比如CAS、ABA问题

单例模式的八种实现方式以及double-checked-locking 重排序 happens-before

HashMap的扩容和碰撞、ConcurrentHashMap的锁分离技术

红黑树的旋转、染色、时间复杂度

CopyOnWrite的原理、应用场景、缺点、需要注意的事情









# 查找Java进程中占用CPU最多的线程

1. 确定进程ID，使用`jps -v`或`top`查看

2. 查看该进程哪个线程占用大量CPU，`top -H -p  [PID]`

3. 将进程中所有线程输出到文件，`jstack [PID] > jstack.txt`

4. 在进程中查找对应的线程ID，`cat jstack.txt | grep -i [TID]`。 

   TID是线程id的16进制表示


[脚手架](https://mp.weixin.qq.com/s/e5y52jp7JFUKDizm8hPGPw)

[脚手架](https://github.com/uniquezhangqi/javaweb)

脚手架 https://github.com/shuzheng/zheng

脚手架 https://github.com/yyhsong/iEasyUI

脚手架 https://www.jianshu.com/p/923d26d705ed

脚手架 https://gitee.com/jobob/jas

脚手架 https://github.com/stylefeng/Guns

https://gitee.com/lcg0124/bootdo

博客系统 https://gitee.com/mtons/mblog

https://github.com/wuyouzhuguli/FEBS-Shiro

https://www.jianshu.com/p/68811cf6dfac

https://github.com/Radom7/springboot-layui

HTML模本脚手架 https://gitee.com/yinqi/Light-Year-Admin-Template

根据接口生成页面 http://gen.sdemo.cn/#/about

图形化前端工具 阿里飞冰 美团乐高

页面可视化搭建工具前生今世 https://github.com/CntChen/cntchen.github.io/issues/15


###### HeadlessBrowsers

https://zhuanlan.zhihu.com/p/25803955

https://github.com/dhamaniasad/HeadlessBrowsers

http://www.testclass.net/selenium_java