Redis
-

### 目录

- 概述

### 概述

Redis是一个开源的、基于内存的结构化数据仓库，被用于数据库、缓存、消息中间件。

Redis支持字符串、哈希表、链表、集合、有序集合等多种数据结构。

Redis有内置的复制、Lua脚本、LRU淘汰、事务、不同级别的磁盘持久化功能，支持通过Redis Sentinel实现高可用，还支持通过Redis Cluster实现数据自动分片。

Redis基于单线程模型实现，通过一个线程处理所有客户端请求，采用了非阻塞式IO，
线程安全且速度快，Redis每秒可以处理10万次读写操作，是已知性能最快的K-V DB。
注：高耗时命令避免执行，会导致所有请求变慢

### 

[部署模式](deployment.md)

[主从复制](https://segmentfault.com/a/1190000018268350)

[相关文档](https://redis.io/topics/replication)

[数据持久化](data_persistence.md)


Redis的数据结构和相关常用命令
数据持久化
内存管理与数据淘汰机制
Pipelining
事务与Scripting
Redis性能调优
主从复制与集群分片
Redis Java客户端的选择

概述

其他常用命令

EXISTS：判断指定的key是否存在，返回1代表存在，0代表不存在，时间复杂度O(1)
DEL：删除指定的key及其对应的value，时间复杂度O(N)，N为删除的key数量
EXPIRE/PEXPIRE：为一个key设置有效期，单位为秒或毫秒，时间复杂度O(1)
TTL/PTTL：返回一个key剩余的有效时间，单位为秒或毫秒，时间复杂度O(1)
RENAME/RENAMENX：将key重命名为newkey。使用RENAME时，如果newkey已经存在，其值会被覆盖；使用RENAMENX时，如果newkey已经存在，则不会进行任何操作，时间复杂度O(1)
TYPE：返回指定key的类型，string, list, set, zset, hash。时间复杂度O(1)
CONFIG GET：获得Redis某配置项的当前值，可以使用*通配符，时间复杂度O(1)
CONFIG SET：为Redis某个配置项设置新值，时间复杂度O(1)
CONFIG REWRITE：让Redis重新加载redis.conf中的配置

内存管理与数据淘汰机制

最大内存设置

默认情况下，在32位OS中，Redis最大使用3GB的内存，在64位OS中则没有限制。
在使用Redis时，应该对数据占用的最大空间有一个基本准确的预估，并为Redis设定最大使用的内存。否则在64位OS中Redis会无限制地占用内存（当物理内存被占满后会使用swap空间），容易引发各种各样的问题。

通过如下配置控制Redis使用的最大内存：

maxmemory 100mb
 
在内存占用达到了maxmemory后，再向Redis写入数据时，Redis会：

根据配置的数据淘汰策略尝试淘汰数据，释放空间
如果没有数据可以淘汰，或者没有配置数据淘汰策略，那么Redis会对所有写请求返回错误，但读请求仍然可以正常执行

在为Redis设置maxmemory时，需要注意：

如果采用了Redis的主从同步，主节点向从节点同步数据时，会占用掉一部分内存空间，如果maxmemory过于接近主机的可用内存，导致数据同步时内存不足。所以设置的maxmemory不要过于接近主机可用的内存，留出一部分预留用作主从同步。

数据淘汰机制

Redis提供了5种数据淘汰策略：

volatile-lru：使用LRU算法进行数据淘汰（淘汰上次使用时间最早的，且使用次数最少的key），只淘汰设定了有效期的key
allkeys-lru：使用LRU算法进行数据淘汰，所有的key都可以被淘汰
volatile-random：随机淘汰数据，只淘汰设定了有效期的key
allkeys-random：随机淘汰数据，所有的key都可以被淘汰
volatile-ttl：淘汰剩余有效期最短的key

最好为Redis指定一种有效的数据淘汰策略以配合maxmemory设置，避免在内存使用满后发生写入失败的情况。

一般来说，推荐使用的策略是volatile-lru，并辨识Redis中保存的数据的重要性。对于那些重要的，绝对不能丢弃的数据（如配置类数据等），应不设置有效期，这样Redis就永远不会淘汰这些数据。对于那些相对不是那么重要的，并且能够热加载的数据（比如缓存最近登录的用户信息，当在Redis中找不到时，程序会去DB中读取），可以设置上有效期，这样在内存不够时Redis就会淘汰这部分数据。

配置方法：

maxmemory-policy volatile-lru   #默认是noeviction，即不进行数据淘汰
 
Pipelining

Pipelining

Redis提供许多批量操作的命令，如MSET/MGET/HMSET/HMGET等等，这些命令存在的意义是减少维护网络连接和传输数据所消耗的资源和时间。

例如连续使用5次SET命令设置5个不同的key，比起使用一次MSET命令设置5个不同的key，效果是一样的，但前者会消耗更多的RTT(Round Trip Time)时长，永远应优先使用后者。

然而，如果客户端要连续执行的多次操作无法通过Redis命令组合在一起，例如：

SET a "abc"
INCR b
HSET c name "hi"

此时便可以使用Redis提供的pipelining功能来实现在一次交互中执行多条命令。

使用pipelining时，只需要从客户端一次向Redis发送多条命令（以rn）分隔，Redis就会依次执行这些命令，并且把每个命令的返回按顺序组装在一起一次返回，比如：

$ (printf "PINGrnPINGrnPINGrn"; sleep 1) | nc localhost 6379
+PONG
+PONG
+PONG

大部分的Redis客户端都对Pipelining提供支持，所以开发者通常并不需要自己手工拼装命令列表。

Pipelining的局限性

Pipelining只能用于执行连续且无相关性的命令，当某个命令的生成需要依赖于前一个命令的返回时，就无法使用Pipelining了。

通过Scripting功能，可以规避这一局限性

事务与Scripting

Pipelining能够让Redis在一次交互中处理多条命令，然而在一些场景下，我们可能需要在此基础上确保这一组命令是连续执行的。

比如获取当前累计的PV数并将其清0

> GET vCount
12384
> SET vCount 0
OK

如果在GET和SET命令之间插进来一个INCR vCount，就会使客户端拿到的vCount不准确。

Redis的事务可以确保复数命令执行时的原子性。也就是说Redis能够保证：一个事务中的一组命令是绝对连续执行的，在这些命令执行完成之前，绝对不会有来自于其他连接的其他命令插进去执行。

通过MULTI和EXEC命令来把这两个命令加入一个事务中：

> MULTI
OK
> GET vCount
QUEUED
> SET vCount 0
QUEUED
> EXEC
1) 12384
2) OK

Redis在接收到MULTI命令后便会开启一个事务，这之后的所有读写命令都会保存在队列中但并不执行，直到接收到EXEC命令后，Redis会把队列中的所有命令连续顺序执行，并以数组形式返回每个命令的返回结果。

可以使用DISCARD命令放弃当前的事务，将保存的命令队列清空。

需要注意的是，Redis事务不支持回滚：

如果一个事务中的命令出现了语法错误，大部分客户端驱动会返回错误，2.6.5版本以上的Redis也会在执行EXEC时检查队列中的命令是否存在语法错误，如果存在，则会自动放弃事务并返回错误。

但如果一个事务中的命令有非语法类的错误（比如对String执行HSET操作），无论客户端驱动还是Redis都无法在真正执行这条命令之前发现，所以事务中的所有命令仍然会被依次执行。在这种情况下，会出现一个事务中部分命令成功部分命令失败的情况，然而与RDBMS不同，Redis不提供事务回滚的功能，所以只能通过其他方法进行数据的回滚。

通过事务实现CAS

Redis提供了WATCH命令与事务搭配使用，实现CAS乐观锁的机制。

假设要实现将某个商品的状态改为已售：

if(exec(HGET stock:1001 state) == "in stock")
    exec(HSET stock:1001 state "sold");

这一伪代码执行时，无法确保并发安全性，有可能多个客户端都获取到了”in stock”的状态，导致一个库存被售卖多次。

使用WATCH命令和事务可以解决这一问题：

exec(WATCH stock:1001);
if(exec(HGET stock:1001 state) == "in stock") {
    exec(MULTI);
    exec(HSET stock:1001 state "sold");
    exec(EXEC);
}

WATCH的机制是：在事务EXEC命令执行时，Redis会检查被WATCH的key，只有被WATCH的key从WATCH起始时至今没有发生过变更，EXEC才会被执行。如果WATCH的key在WATCH命令到EXEC命令之间发生过变化，则EXEC命令会返回失败。

Scripting

通过EVAL与EVALSHA命令，可以让Redis执行LUA脚本。这就类似于RDBMS的存储过程一样，可以把客户端与Redis之间密集的读/写交互放在服务端进行，避免过多的数据交互，提升性能。

Scripting功能是作为事务功能的替代者诞生的，事务提供的所有能力Scripting都可以做到。Redis官方推荐使用LUA Script来代替事务，前者的效率和便利性都超过了事务。

关于Scripting的具体使用，本文不做详细介绍，请参考官方文档 
https://redis.io/commands/eval


[Redis优化（未完成）](optimize.md)

Redis Java客户端的选择

Redis的Java客户端很多，官方推荐的有三种：Jedis、Redisson和lettuce。

在这里对Jedis和Redisson进行对比介绍

Jedis：

轻量，简洁，便于集成和改造
支持连接池
支持pipelining、事务、LUA Scripting、Redis Sentinel、Redis Cluster
不支持读写分离，需要自己实现
文档差（真的很差，几乎没有……）

Redisson：

基于Netty实现，采用非阻塞IO，性能高
支持异步请求
支持连接池
支持pipelining、LUA Scripting、Redis Sentinel、Redis Cluster
不支持事务，官方建议以LUA Scripting代替事务
支持在Redis Cluster架构下使用pipelining
支持读写分离，支持读负载均衡，在主从复制和Redis Cluster架构下都可以使用
内建Tomcat Session Manager，为Tomcat 6/7/8提供了会话共享功能
可以与Spring Session集成，实现基于Redis的会话共享
文档较丰富，有中文文档

对于Jedis和Redisson的选择，同样应遵循前述的原理，尽管Jedis比起Redisson有各种各样的不足，但也应该在需要使用Redisson的高级特性时再选用Redisson，避免造成不必要的程序复杂度提升。


基于内存的数据存储媒介，每秒可处理10万次读写操作，是已知性能最快的k-v

# Redis

## 简介与优势



支持多种数据结构，V最大限制1GB（memcached最大1MB）。
List可以做双向链表，实现高性能消息队列、Set可以做高性能tag系统。

缺点：数据库容量受到物理内存影响。

优点：

	1 基于内存的K-V数据库，速度快且时间复杂度都是O（1）
	2 支持String、List、Set、Sorted Set、Hash
	3 支持事物
	4 可以设置过期时间，用于缓存、消息
	

## 常见性能问题和解决方案

Master不做任何持久化工作。

若数据重要，某个Slave开启AOF，策略每秒一次。

为保证主从复制的速度和连接稳定性，Master和Slave最好在同一局域网。

避免在压力比较大的主库增加从库。

主从复制使用单向链表更为稳定。

## 适用场景

	1 Session Cache
	2 队列
	3 全页缓存 FPC
	4 排行榜/计数器 （Set和Sorted Set）
	


[数据类型](data_type.md)

> 官方网站 https://redis.io/

> 在线测试 http://try.redis.io/