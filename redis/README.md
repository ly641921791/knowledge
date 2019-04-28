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

List

Redis的List是链表型的数据结构，可以使用LPUSH/RPUSH/LPOP/RPOP等命令在List的两端执行插入元素和弹出元素的操作。虽然List也支持在特定index上插入和读取元素的功能，但其时间复杂度较高（O(N)），应小心使用。

与List相关的常用命令：

LPUSH：向指定List的左侧（即头部）插入1个或多个元素，返回插入后的List长度。时间复杂度O(N)，N为插入元素的数量
RPUSH：同LPUSH，向指定List的右侧（即尾部）插入1或多个元素
LPOP：从指定List的左侧（即头部）移除一个元素并返回，时间复杂度O(1)
RPOP：同LPOP，从指定List的右侧（即尾部）移除1个元素并返回
LPUSHX/RPUSHX：与LPUSH/RPUSH类似，区别在于，LPUSHX/RPUSHX操作的key如果不存在，则不会进行任何操作
LLEN：返回指定List的长度，时间复杂度O(1)
LRANGE：返回指定List中指定范围的元素（双端包含，即LRANGE key 0 10会返回11个元素），时间复杂度O(N)。应尽可能控制一次获取的元素数量，一次获取过大范围的List元素会导致延迟，同时对长度不可预知的List，避免使用LRANGE key 0 -1这样的完整遍历操作。

应谨慎使用的List相关命令：

LINDEX：返回指定List指定index上的元素，如果index越界，返回nil。index数值是回环的，即-1代表List最后一个位置，-2代表List倒数第二个位置。时间复杂度O(N)
LSET：将指定List指定index上的元素设置为value，如果index越界则返回错误，时间复杂度O(N)，如果操作的是头/尾部的元素，则时间复杂度为O(1)
LINSERT：向指定List中指定元素之前/之后插入一个新元素，并返回操作后的List长度。如果指定的元素不存在，返回-1。如果指定key不存在，不会进行任何操作，时间复杂度O(N)

由于Redis的List是链表结构的，上述的三个命令的算法效率较低，需要对List进行遍历，命令的耗时无法预估，在List长度大的情况下耗时会明显增加，应谨慎使用。

换句话说，Redis的List实际是设计来用于实现队列，而不是用于实现类似ArrayList这样的列表的。如果你不是想要实现一个双端出入的队列，那么请尽量不要使用Redis的List数据结构。

为了更好支持队列的特性，Redis还提供了一系列阻塞式的操作命令，如BLPOP/BRPOP等，能够实现类似于BlockingQueue的能力，即在List为空时，阻塞该连接，直到List中有对象可以出队时再返回。针对阻塞类的命令，此处不做详细探讨，请参考官方文档（https://redis.io/topics/data-types-intro） 中”Blocking operations on lists”一节。

Hash

Hash即哈希表，Redis的Hash和传统的哈希表一样，是一种field-value型的数据结构，可以理解成将HashMap搬入Redis。

Hash非常适合用于表现对象类型的数据，用Hash中的field对应对象的field即可。

Hash的优点包括：

可以实现二元查找，如”查找ID为1000的用户的年龄”
比起将整个对象序列化后作为String存储的方法，Hash能够有效地减少网络传输的消耗
当使用Hash维护一个集合时，提供了比List效率高得多的随机访问命令

与Hash相关的常用命令：

HSET：将key对应的Hash中的field设置为value。如果该Hash不存在，会自动创建一个。时间复杂度O(1)
HGET：返回指定Hash中field字段的值，时间复杂度O(1)
HMSET/HMGET：同HSET和HGET，可以批量操作同一个key下的多个field，时间复杂度：O(N)，N为一次操作的field数量
HSETNX：同HSET，但如field已经存在，HSETNX不会进行任何操作，时间复杂度O(1)
HEXISTS：判断指定Hash中field是否存在，存在返回1，不存在返回0，时间复杂度O(1)
HDEL：删除指定Hash中的field（1个或多个），时间复杂度：O(N)，N为操作的field数量
HINCRBY：同INCRBY命令，对指定Hash中的一个field进行INCRBY，时间复杂度O(1)

应谨慎使用的Hash相关命令：

HGETALL：返回指定Hash中所有的field-value对。返回结果为数组，数组中field和value交替出现。时间复杂度O(N)
HKEYS/HVALS：返回指定Hash中所有的field/value，时间复杂度O(N)

上述三个命令都会对Hash进行完整遍历，Hash中的field数量与命令的耗时线性相关，对于尺寸不可预知的Hash，应严格避免使用上面三个命令，而改为使用HSCAN命令进行游标式的遍历，具体请见 
https://redis.io/commands/scan
Set

Redis Set是无序的，不可重复的String集合。

与Set相关的常用命令：

SADD：向指定Set中添加1个或多个member，如果指定Set不存在，会自动创建一个。时间复杂度O(N)，N为添加的member个数
SREM：从指定Set中移除1个或多个member，时间复杂度O(N)，N为移除的member个数
SRANDMEMBER：从指定Set中随机返回1个或多个member，时间复杂度O(N)，N为返回的member个数
SPOP：从指定Set中随机移除并返回count个member，时间复杂度O(N)，N为移除的member个数
SCARD：返回指定Set中的member个数，时间复杂度O(1)
SISMEMBER：判断指定的value是否存在于指定Set中，时间复杂度O(1)
SMOVE：将指定member从一个Set移至另一个Set

慎用的Set相关命令：

SMEMBERS：返回指定Hash中所有的member，时间复杂度O(N)
SUNION/SUNIONSTORE：计算多个Set的并集并返回/存储至另一个Set中，时间复杂度O(N)，N为参与计算的所有集合的总member数
SINTER/SINTERSTORE：计算多个Set的交集并返回/存储至另一个Set中，时间复杂度O(N)，N为参与计算的所有集合的总member数
SDIFF/SDIFFSTORE：计算1个Set与1或多个Set的差集并返回/存储至另一个Set中，时间复杂度O(N)，N为参与计算的所有集合的总member数

上述几个命令涉及的计算量大，应谨慎使用，特别是在参与计算的Set尺寸不可知的情况下，应严格避免使用。可以考虑通过SSCAN命令遍历获取相关Set的全部member（具体请见 https://redis.io/commands/scan ），如果需要做并集/交集/差集计算，可以在客户端进行，或在不服务实时查询请求的Slave上进行。

Sorted Set

Redis Sorted Set是有序的、不可重复的String集合。Sorted Set中的每个元素都需要指派一个分数(score)，Sorted Set会根据score对元素进行升序排序。如果多个member拥有相同的score，则以字典序进行升序排序。

Sorted Set非常适合用于实现排名。

Sorted Set的主要命令：

ZADD：向指定Sorted Set中添加1个或多个member，时间复杂度O(Mlog(N))，M为添加的member数量，N为Sorted Set中的member数量
ZREM：从指定Sorted Set中删除1个或多个member，时间复杂度O(Mlog(N))，M为删除的member数量，N为Sorted Set中的member数量
ZCOUNT：返回指定Sorted Set中指定score范围内的member数量，时间复杂度：O(log(N))
ZCARD：返回指定Sorted Set中的member数量，时间复杂度O(1)
ZSCORE：返回指定Sorted Set中指定member的score，时间复杂度O(1)
ZRANK/ZREVRANK：返回指定member在Sorted Set中的排名，ZRANK返回按升序排序的排名，ZREVRANK则返回按降序排序的排名。时间复杂度O(log(N))
ZINCRBY：同INCRBY，对指定Sorted Set中的指定member的score进行自增，时间复杂度O(log(N))

慎用的Sorted Set相关命令：

ZRANGE/ZREVRANGE：返回指定Sorted Set中指定排名范围内的所有member，ZRANGE为按score升序排序，ZREVRANGE为按score降序排序，时间复杂度O(log(N)+M)，M为本次返回的member数
ZRANGEBYSCORE/ZREVRANGEBYSCORE：返回指定Sorted Set中指定score范围内的所有member，返回结果以升序/降序排序，min和max可以指定为-inf和+inf，代表返回所有的member。时间复杂度O(log(N)+M)
ZREMRANGEBYRANK/ZREMRANGEBYSCORE：移除Sorted Set中指定排名范围/指定score范围内的所有member。时间复杂度O(log(N)+M)

上述几个命令，应尽量避免传递[0 -1]或[-inf +inf]这样的参数，来对Sorted Set做一次性的完整遍历，特别是在Sorted Set的尺寸不可预知的情况下。可以通过ZSCAN命令来进行游标式的遍历（具体请见 https://redis.io/commands/scan ），或通过LIMIT参数来限制返回member的数量（适用于ZRANGEBYSCORE和ZREVRANGEBYSCORE命令），以实现游标式的遍历。

Bitmap和HyperLogLog

Redis的这两种数据结构相较之前的并不常用，在本文中只做简要介绍，如想要详细了解这两种数据结构与其相关的命令，请参考官方文档
https://redis.io/topics/data-types-intro 中的相关章节

Bitmap在Redis中不是一种实际的数据类型，而是一种将String作为Bitmap使用的方法。可以理解为将String转换为bit数组。使用Bitmap来存储true/false类型的简单数据极为节省空间。

HyperLogLogs是一种主要用于数量统计的数据结构，它和Set类似，维护一个不可重复的String集合，但是HyperLogLogs并不维护具体的member内容，只维护member的个数。也就是说，HyperLogLogs只能用于计算一个集合中不重复的元素数量，所以它比Set要节省很多内存空间。

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
	
## 两种持久化方式

	RDB（Redis DataBase）：定时生成数据快照存储。
	AOF（Append Only File）：记录全部运行指令。
	
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
	
## 高可用策略

	主备：一般通过keepalived对外提供虚拟同一ip，并监控主备服务器。缺点是备机资源浪费。
	主从：可以进行读写分离。缺点是Master挂了，需要通知客户端新的Master地址。
	
