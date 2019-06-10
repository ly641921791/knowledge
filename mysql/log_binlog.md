二进制日志（binary log）
-

用于记录数据库变化的日志，可用于数据库恢复、数据同步

###### 配置二进制日志

默认情况下，二进制日志是关闭的，通过修改my.ini文件下[MySQLd]下的组进行配置

```ini
log-bin [=path/[filename]]
expire_logs_days = 10
max_binlog_size = 100M
```

- log-bin ：开启二进制日志。path表示日志存放目录，filename表示日志文件名。配置后，日志目录会生成filename.index文件用于记录日志清单，
文件名为filename.000001，每次重启或文件大小达到上限都会重新生成文件并将序列+1。默认为主机名
- expire_logs_days ：日志文件存放天数，默认0不删除
- max_binlog_size ：日志文件大小上限，默认1GB。取值范围[4096B,1GB]。大事务会导致文件超过max_binlog_size

配置并重启，可以通过`SHOW VARIABLES LIKE 'log_%'`查看配置是否生效，`log_bin=ON`表示二进制日志打开

###### 删除二进制日志

1. `RESET MASTER`

删除全部二进制日志，新的日志编号从1开始

2. `PURGE MASTER LOGS`

可以删除指定文件之前或指定日期之前的日志

删除日志后，可以通过`SHOW BINARY LOGS`查看二进制列表

###### 其他

通过二进制日志可以恢复数据库，通过命令可以暂时启停二进制日志

###### 应用场景

主从复制：通过主从复制实现灾难恢复、水平扩展、统计分析、远程数据分发等

读写分离：基于主从复制。master执行更新操作，slave执行读取操作。若使用多个slave，可在slave上实现负载均衡。通常会借助数据库中间件，
如：tddl、sharding-jdbc

数据恢复：通过反解binlog实现数据恢复

数据一致性：对于数据库和其他数据存储介质的一致性，无法保证操作同时成功。利用binlog同步组件模拟slave拉取master的binlog，再配合MQ或其他中间件
同步数据，常见中间件：linkedin的databus、阿里巴巴的canal、美团点评的puma

异步多活：多个数据中心中间的数据同步（A、B两个数据中心互相同步数据）。需要解决数据冲突问题（主键、唯一索引冲突）、数据回环问题（A的数据同步到B还会产生binlog）。
解决的中间件：阿里巴巴的otter、美团的DRC

###### 工作流程

1. master每次准备提交事务完成数据更新前，将改变记录到二进制日志中。这些记录叫做二进制日志时间，binary log event，简称event
2. slave启动一个I/O线程来读取主库binary log中的事件，并记录到slave自己的中继日志中，relay log
3. slave启动一个SQL线程，从relay log中读取事件并执行，实现数据更新
