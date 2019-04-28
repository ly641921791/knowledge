事务隔离级别
-

###### 数据库四种隔离级别

1. 可读未提交（READ UNCOMMITTED）

写事务阻止其他写事务，避免更新丢失；不阻止其他读事务

存在问题：脏读。读取到另一个事务未提交的数据

2. 可读已提交（READ COMMITTED）

Sql Server，Oracle的默认隔离级别

写事务阻止其他读写事务；读事务不阻止其他事务

存在问题：不可重复读。读事务不阻止其他事务，若两次读取中间存在提交，会导致读取不一致

3. 可重复读（REPEATABLE READ）

MySQL的默认隔离级别

读事务阻止其他写事务，但不会阻止其他读事务

存在问题：幻读。对存在数据加锁，因此存在幻读

4. 序列化（SERIALIZABLE）

读加共享锁；写加排它锁。读事务可并发，写事务互斥。

###### 具体实现

MySql的InnoDB的事物通过InnoDB日志和锁实现。

隔离性通过锁实现

持久性通过Redo Log实现

原子性和一致性通过Undo Log实现

操作数据之前，将数据备份到Undo Log中，然后操作数据，若回滚则利用Undo Log中的数据恢复，Redo Log记录新数据，提交事物即将Redo Log持久化

###### 存在问题

四种隔离级别通过不同的锁实现的，存在以下问题

|隔离级别|脏读|不可重复读|幻读|
|---|---|---|---|
|READ UNCOMMITTED|√|√|√|
|READ COMMITTED|×|√|√|
|REPEATABLE READ|×|×|√|
|SERIALIZABLE|×|×|×|

脏读：事务A读到了事物B未提交的数据
不可重复读：事物A读取同一条数据的多次，期间事物B操作了该数据，导致多次读取到的数据不一致
幻读：事物A读取同一批数据多次，期间事物B插入或删除了该数据，导致多次读取到的数据量不一致

> 参考文档：https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-isolation-levels.html