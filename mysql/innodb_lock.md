锁
-


##### 常见问题

###### 并发插入引起死锁

项目中使用到了排行榜的功能，由于排行榜需要根据多个指标排名，因此不能通过Redis的zset实现，决定通过数据库实现（存储引擎InnoDB）

有一个接口是为参与者新增排行榜记录或对现有记录增加积分，测试过程发现，并发新增排行记录会产生死锁

通过查看死锁信息（命令：SHOW ENGINE INNODB STATUS），发现是自增主键引起的死锁，将自增主键切换为自定义主键后依然死锁

查看相关文章得知，两个锁的特性

- 当对存在的记录进行锁的时候（主键），只有行锁
- 当对不存在的记录进行锁的时候（即使条件为主键），会锁住一段范围

第二个特性表明，普通的插入语句并不是行锁，这也就是并发插入会产生死锁的原因。同时，文章表示，
使用`INSERT INTO tbl(xxx,xxx...) ON DUPLICATE KEY UPDATE xxx=xxx...`，不管插入的行存不存在，都只有行锁，将SQL改造后解决了死锁问题

> 参考文章 https://www.cnblogs.com/zejin2008/p/5262751.html
>
> 死锁分析博主 https://www.jianshu.com/p/7922f8b0e678
>
> 官网文档 https://dev.mysql.com/doc/refman/8.0/en/insert-on-duplicate.html