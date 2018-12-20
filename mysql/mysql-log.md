[TOC]

# MySQL日志

## 一 日志分类

错误日志、二进制日志、通用查询日志、慢查询日志等

### 1 通用查询日志

记录建立的客户端连接和查询语句

### 2 慢查询日志

记录所有执行时间超过long_query_time秒的查询

## 二 通用查询日志

###1 查看、设置启动状态

**查看**

执行`show variables like '%general%';`

general_log为ON表示开启，为OFF表示关闭。默认关闭。

**设置**

临时开启：`set global general_log=on;`

临时关闭：`set global general_log=off;`

永久开启：my.cnf -> general_log=1

永久关闭：my.cnf -> general_log=0

### 2 查看、设置日志输出格式

**查看**

执行`show variables like '%log_output%';`

FILE表示存储在数据库数据文件的hostname.log中，TABLE表示存储在mysql.general_log表

**设置**

临时设置输出表：`set global log_output='TABLE';`

临时设置输出文件：`set global log_output='FILE';`

临时设置输出表和文件：`set global log_output='FILE,TABLE';`

永久设置输出表和文件：my.cnf -> log_output=FILE,TABLE

## 三 慢查询日志

记录运行时间超过long_query_time的SQL。要求高性能的话，建议写文件。默认不开启。默认10S，通常设置1S。

### 1 查看启动状态

**查看**

执行`show variables like '%quer%';`

- show_query_log ：ON表示开启，OFF表示关闭

- show_query_log_file ：日志写入的文件
- long_query_time ：表示慢查询的阈值
- log_queries_not_using_indexes ：记录所有没有利用索引的查询

慢查询日志会输出到mysql.slow_log表中

查询当前慢查询的语句数量 ：`show golbal status like '%slow%';`















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