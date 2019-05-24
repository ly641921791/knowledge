MySQL
-

- [Docker安装](docker.md)
- [MySQL日志](log.md)
- [主从复制](replication.md)

[SQL执行流程](sql_execution_flow.md)

[事物隔离级别（未完成）](innodb_transaction_isoliation_levels.md)

[索引](index.md)




[多主一从配置](https://my.oschina.net/u/2399373/blog/2878650)

[优惠券系统设计](https://juejin.im/post/5c88606c6fb9a049cb199c04)





###### 并发插入引起的死锁问题

查询死锁信息：`SHOW ENGINE INNODB STATUS`

参考文章说了两个锁的特性

- 当对存在的记录进行锁的时候（主键），只有行锁
- 当对不存在的记录进行锁的时候（即使条件为主键），会锁住一段范围

同时文章表示，使用`INSERT INTO tbl(xxx,xxx...) ON DUPLICATE KEY UPDATE xxx=xxx...`，不管插入的行存不存在，都只有行锁

使用该语句解决了死锁问题

> 参考文章 https://www.cnblogs.com/zejin2008/p/5262751.html
>
> 死锁分析博主 https://www.jianshu.com/p/7922f8b0e678