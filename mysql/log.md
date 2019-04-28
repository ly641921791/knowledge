MySQL日志
-

### 日志简介

MySQL日志主要分为4类，分表是：

- 错误日志：记录使用MySQL出现的问题日志
- 查询日志：记录客户端连接和执行语句
- 二进制日志：记录更改数据的日志，可用于数据复制
- 慢查询日志：记录执行时间超过long_query_time和不使用索引的查询

### 日志详解

#### 二进制日志

二进制日志主要是记录数据库变化的日志，可用于数据库恢复、多个数据库间数据同步

##### 配置

默认情况下，二进制日志是关闭的，通过修改my.ini文件下[MySQLd]下的组进行配置

```ini
log-bin [=path/[filename]]
expire_logs_days = 10
max_binlog_size = 100M
```

`log-bin`表示开启二进制日志

`path`表示日志存放目录

`filename`表示日志文件名，生成的文件如：filename.000001，每次重启或文件大小达到上限都会重新生成文件并将序列+1，该目录还会生成filename.index文件记录所有日志的清单。默认为主机名

`expire_logs_days`表示日志文件存放天数，默认0不删除

`max_binlog_size`表示单个日志文件的大小，默认1GB。4096B < max_binlog_size < 1GB。大事物会导致文件大小超过`max_binlog_size`

配置完成并重启MySQL后，可以通过`SHOW VARIABLES LIKE 'log_%'`查看配置是否生效，`log_bin=ON`表示二进制日志打开

##### 删除

1. `RESET MASTER`

删除全部二进制日志，新的日志编号从1开始

2. `PURGE MASTER LOGS`

可以删除指定文件之前或指定日期之前的日志

删除日志后，可以通过`SHOW BINARY LOGS`查看二进制列表

##### 其他

通过二进制日志可以恢复数据库，通过命令可以暂时启停二进制日志

#### 错误日志

错误日志记录了MySQL启停和发送错误时的信息

- 配置

默认情况下，存放在在数据目录下，文件名为hostname.err，通过修改配置文件[MySQLd]下的组进行配置

```ini
[mysqld]
log-error [=path/[filename]]
```

- 查看

执行`SHOW VARIABLES LIKE 'log_error'`获得错误日志的文件地址，使用记事本打开

- 删除

执行`flush logs`或直接删除

#### 查询日志

记录了用户的所有操作：启停服务、查询、更新数据等

- 配置

默认情况下，存放在数据目录下，文件名为hostname.log，不开启，通过修改配置文件[MySQLd]下的组进行配置

```ini
[mysqld]
log [=path/[filename]]
```

执行`show variables like '%general%';`

general_log为ON表示开启，为OFF表示关闭。默认关闭。

临时开启：`set global general_log=on;`

临时关闭：`set global general_log=off;`

永久开启：my.cnf -> general_log=1

永久关闭：my.cnf -> general_log=0

- 查看

可以使用记事本打开

执行`show variables like '%log_output%';`

FILE表示存储在数据库数据文件的hostname.log中，TABLE表示存储在mysql.general_log表

临时设置输出表：`set global log_output='TABLE';`

临时设置输出文件：`set global log_output='FILE';`

临时设置输出表和文件：`set global log_output='FILE,TABLE';`

永久设置输出表和文件：my.cnf -> log_output=FILE,TABLE

- 删除

执行`flush logs`或直接删除

#### 三 慢查询日志

用于记录查询时间超过long_query_time的SQL，然后进行优化，要求高性能的话，建议写文件。默认不开启。默认10S，通常设置1S。

##### 配置

默认情况下，是关闭的，通过如下配置打开

```ini
[mysqld]
log-slow-queries [=path/filename]
long_query_time=n
```

执行`show variables like '%quer%';`

- show_query_log ：ON表示开启，OFF表示关闭

- show_query_log_file ：日志写入的文件
- long_query_time ：表示慢查询的阈值
- log_queries_not_using_indexes ：记录所有没有利用索引的查询

慢查询日志会输出到mysql.slow_log表中

查询当前慢查询的语句数量 ：`show golbal status like '%slow%';`

删除同上















补充知识点：如何利用MySQL自带的慢查询日志分析工具mysqldumpslow分析日志？

perlmysqldumpslow –s c –t 10 slow-query.log

具体参数设置如下：

-s 表示按何种方式排序，c、t、l、r分别是按照记录次数、时间、查询时间、返回的记录数来排序，ac、at、al、ar，表示相应的倒叙；

-t 表示top的意思，后面跟着的数据表示返回前面多少条；

-g 后面可以写正则表达式匹配，大小写不敏感。

![img](https://mmbiz.qpic.cn/mmbiz_jpg/WwPkUCFX4x7hXZ98kcPDzicr6qFuBBLaWZ16Sco9Rp4agA3IFjL5SE8eznQ4uRcCHicY6PBxW7nkHZ6mToPuumJQ/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1&wx_co=1)

上述中的参数含义如下：

Count:414 语句出现了414次；

Time=3.51s（1454） 执行最长时间为3.51s，累计总耗费时间1454s；

Lock=0.0s（0） 等待锁最长时间为0s，累计等待锁耗费时间为0s；

Rows=2194.9（9097604） 发送给客户端最多的行数为2194.9，累计发送给客户端的函数为90976404

（注意：mysqldumpslow脚本是用perl语言写的，具体mysqldumpslow的用法后期再讲）

问题：实际在学习过程中，如何得知设置的慢查询是有效的？

很简单，我们可以手动产生一条慢查询语句，比如，如果我们的慢查询log_query_time的值设置为1，则我们可以执行如下语句：

selectsleep(1);

该条语句即是慢查询语句，之后，便可以在相应的日志输出文件或表中去查看是否有该条语句。



[修改数据与原数据一致，会执行吗](https://mp.weixin.qq.com/s/2nd-Fm8WjWeIA3iv-JE6pg)
