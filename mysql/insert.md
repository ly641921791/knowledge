INSERT
-

INSERT ... ON DUPLICATE KEY UPDATE ...

执行时，MySQL会检查插入数据是否存在重复KEY（主键、唯一索引等）错误，若存在，将数据加S锁，然后更新数据，加X锁，最后写入更新

###### 等价语句

以下面的语句为例

``` mysql
INSERT INTO t1 (a,b,c) VALUES (1,2,3) 
ON DUPLICATE KEY UPDATE c=c+1;
```

当a列添加唯一索引，存在冲突时，等价于下面语句

``` mysql
UPDATE t1 SET c=c+1 WHERE a=1;
```

当a、b列分别添加唯一索引，存在冲突时，等价于下面语句

``` mysql
UPDATE t1 SET c=c+1 WHERE a=1 OR b=2 LIMIT 1;
```

只有第一条记录被更新，因此应该避免这种情况

###### 修改记录数

插入成功 1

执行更新语句导致记录有修改 2

执行更新语句但记录未修改 0。某些配置下，这种情况修改记录数为1

注 ：若数据有多个唯一键，并发时可能产生死锁

> 官网文档 https://dev.mysql.com/doc/refman/8.0/en/insert-on-duplicate.html