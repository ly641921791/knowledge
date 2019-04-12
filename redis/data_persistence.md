数据持久化
-

Redis提供了两种数据持久化方案，同时使用时，AOF文件会被用于恢复数据，因为AOF的数据更为完整

- RDB ：定时保存数据快照，启动时通过数据快照恢复数据
- AOF ：记录全部写操作，启动时执行全部写操作恢复数据

### RDB

*相关配置*

RDB的相关配置只有`save`，可以配置多次。

语法：`save [seconds] [changes]`

含义：在`seconds`秒内发生了`changes`次数据修改，则保存一次数据快照。

例如：`save 60 100`表示每60秒检查一次，若发生100次以上数据变更，则保持快照

配置`save ""`表示关闭RDB。（记得注释掉其他save配置）

RDB默认开启，默认策略如下：

```conf
save 900 1
save 300 10
save 60 10000
```

*命令关闭*

执行`config set save ""`

*命令触发*

- save 使用主线程保存快照，会导致其他请求阻塞，成功后返回OK
- bgsave 使用子线程异步保存快照，可以使用`lastsave`命令查询是否成功，该命令可以理解为background save

*优点*

1. 性能影响小。通过子进程保存快照，几乎不影响处理请求
2. 适合灾难恢复。每个快照文件都有完整的数据，可以将快照文件保存用于灾难恢复
3. 恢复速度相比AOF更快

*缺点*

1. 数据丢失。由于是定期保存快照，存在数据丢失的问题
2. Redis需要fork()才能进行持久化保存，当数据较大时，fork()会比较耗时，影响客户端请求。

### AOF 

AOF是Append Only File的简写，默认关闭，默认的存储策略是每秒同步一次写操作到文件，同时提供了日志重写功能，当日志文件较大时，会通过日志重写
得到一个最小的日志文件

*相关配置*

- appendonly yes/no 配置开启/关闭
- appendfsync no/always/everysec 理解为append file sync，即文件同步策略
	- no 不进行文件同步，将flush文件交给OS处理，速度最快
	- always 每写入一个日志就进行一次文件同步
	- everysec 每秒进行一次文件同步

*日志重写配置*	

```conf
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
```

Redis在每次AOF rewrite时，会记录完成rewrite后的AOF日志大小，上面两行配置的含义是，当AOF日志大小在该基础上增长了100%后，自动进行AOF rewrite。
同时如果增长的大小没有达到64mb，则不会进行rewrite。

*命令触发日志重写*

- bgrewriteaof 类似bgsave

*优点*

1. 安全性。最多丢失一秒数据
2. 自动修复。继续日志写入出现问题，也可以自动修复
3. 自动重写。当日志文件较大时，自动重写将文件变小
4. 易读性。可以将AOF文件导出修复用于恢复数据

*缺点*

1. 文件相比RDB文件更大
2. 性能消耗较RDB高
3. 恢复速度较RDB慢

> 官方文档：https://redis.io/topics/persistence