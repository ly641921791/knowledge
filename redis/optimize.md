Redis优化
-

1. 避免长耗时命令

由于Redis是单线程执行命令，若某个命令耗时较长，会影响其他命令执行



针对Redis的性能优化，主要从下面几个层面入手：

最初的也是最重要的，确保没有让Redis执行耗时长的命令
使用pipelining将连续执行的命令组合执行
操作系统的Transparent huge pages功能必须关闭：

echo never > /sys/kernel/mm/transparent_hugepage/enabled
 
如果在虚拟机中运行Redis，可能天然就有虚拟机环境带来的固有延迟。可以通过./redis-cli –intrinsic-latency 100命令查看固有延迟。同时如果对Redis的性能有较高要求的话，应尽可能在物理机上直接部署Redis。
检查数据持久化策略
考虑引入读写分离机制

长耗时命令

Redis绝大多数读写命令的时间复杂度都在O(1)到O(N)之间，在文本和官方文档中均对每个命令的时间复杂度有说明。

通常来说，O(1)的命令是安全的，O(N)命令在使用时需要注意，如果N的数量级不可预知，则应避免使用。例如对一个field数未知的Hash数据执行HGETALL/HKEYS/HVALS命令，通常来说这些命令执行的很快，但如果这个Hash中的field数量极多，耗时就会成倍增长。

又如使用SUNION对两个Set执行Union操作，或使用SORT对List/Set执行排序操作等时，都应该严加注意。

避免在使用这些O(N)命令时发生问题主要有几个办法：

不要把List当做列表使用，仅当做队列来使用
通过机制严格控制Hash、Set、Sorted Set的大小
可能的话，将排序、并集、交集等操作放在客户端执行
绝对禁止使用KEYS命令
避免一次性遍历集合类型的所有成员，而应使用SCAN类的命令进行分批的，游标式的遍历

Redis提供了SCAN命令，可以对Redis中存储的所有key进行游标式的遍历，避免使用KEYS命令带来的性能问题。同时还有SSCAN/HSCAN/ZSCAN等命令，分别用于对Set/Hash/Sorted Set中的元素进行游标式遍历。SCAN类命令的使用请参考官方文档：
https://redis.io/commands/scan

Redis提供了Slow Log功能，可以自动记录耗时较长的命令。相关的配置参数有两个：

slowlog-log-slower-than xxxms  #执行时间慢于xxx毫秒的命令计入Slow Log
slowlog-max-len xxx  #Slow Log的长度，即最大纪录多少条Slow Log

使用SLOWLOG GET [number]命令，可以输出最近进入Slow Log的number条命令。

使用SLOWLOG RESET命令，可以重置Slow Log

网络引发的延迟

尽可能使用长连接或连接池，避免频繁创建销毁连接
客户端进行的批量数据操作，应使用Pipeline特性在一次交互中完成。具体请参照本文的Pipelining章节
数据持久化引发的延迟

Redis的数据持久化工作本身就会带来延迟，需要根据数据的安全级别和性能要求制定合理的持久化策略：

AOF + fsync always的设置虽然能够绝对确保数据安全，但每个操作都会触发一次fsync，会对Redis的性能有比较明显的影响
AOF + fsync every second是比较好的折中方案，每秒fsync一次
AOF + fsync never会提供AOF持久化方案下的最优性能
使用RDB持久化通常会提供比使用AOF更高的性能，但需要注意RDB的策略配置
每一次RDB快照和AOF Rewrite都需要Redis主进程进行fork操作。fork操作本身可能会产生较高的耗时，与CPU和Redis占用的内存大小有关。根据具体的情况合理配置RDB快照和AOF Rewrite时机，避免过于频繁的fork带来的延迟

Redis在fork子进程时需要将内存分页表拷贝至子进程，以占用了24GB内存的Redis实例为例，共需要拷贝24GB / 4kB * 8 = 48MB的数据。在使用单Xeon 2.27Ghz的物理机上，这一fork操作耗时216ms。

可以通过INFO命令返回的latest_fork_usec字段查看上一次fork操作的耗时（微秒）

Swap引发的延迟

当Linux将Redis所用的内存分页移至swap空间时，将会阻塞Redis进程，导致Redis出现不正常的延迟。Swap通常在物理内存不足或一些进程在进行大量I/O操作时发生，应尽可能避免上述两种情况的出现。

/proc/<pid>/smaps文件中会保存进程的swap记录，通过查看这个文件，能够判断Redis的延迟是否由Swap产生。如果这个文件中记录了较大的Swap size，则说明延迟很有可能是Swap造成的。

数据淘汰引发的延迟

当同一秒内有大量key过期时，也会引发Redis的延迟。在使用时应尽量将key的失效时间错开。

引入读写分离机制

Redis的主从复制能力可以实现一主多从的多节点架构，在这一架构下，主节点接收所有写请求，并将数据同步给多个从节点。

在这一基础上，我们可以让从节点提供对实时性要求不高的读请求服务，以减小主节点的压力。

尤其是针对一些使用了长耗时命令的统计类任务，完全可以指定在一个或多个从节点上执行，避免这些长耗时命令影响其他请求的响应。

关于读写分离的具体说明，请参见后续章节

主从复制与集群分片

主从复制

Redis支持一主多从的主从复制架构。一个Master实例负责处理所有的写请求，Master将写操作同步至所有Slave。

借助Redis的主从复制，可以实现读写分离和高可用：

实时性要求不是特别高的读请求，可以在Slave上完成，提升效率。特别是一些周期性执行的统计任务，这些任务可能需要执行一些长耗时的Redis命令，可以专门规划出1个或几个Slave用于服务这些统计任务
借助Redis Sentinel可以实现高可用，当Master crash后，Redis Sentinel能够自动将一个Slave晋升为Master，继续提供服务

启用主从复制非常简单，只需要配置多个Redis实例，在作为Slave的Redis实例中配置：

slaveof 192.168.1.1 6379  #指定Master的IP和端口
 
当Slave启动后，会从Master进行一次冷启动数据同步，由Master触发BGSAVE生成RDB文件推送给Slave进行导入，导入完成后Master再将增量数据通过Redis Protocol同步给Slave。之后主从之间的数据便一直以Redis Protocol进行同步

使用Sentinel做自动failover

Redis的主从复制功能本身只是做数据同步，并不提供监控和自动failover能力，要通过主从复制功能来实现Redis的高可用，还需要引入一个组件：Redis Sentinel

Redis Sentinel是Redis官方开发的监控组件，可以监控Redis实例的状态，通过Master节点自动发现Slave节点，并在监测到Master节点失效时选举出一个新的Master，并向所有Redis实例推送新的主从配置。

Redis Sentinel需要至少部署3个实例才能形成选举关系。

关键配置：



另外需要注意的是，Redis Sentinel实现的自动failover不是在同一个IP和端口上完成的，也就是说自动failover产生的新Master提供服务的IP和端口与之前的Master是不一样的，所以要实现HA，还要求客户端必须支持Sentinel，能够与Sentinel交互获得新Master的信息才行。

集群分片

为何要做集群分片：

Redis中存储的数据量大，一台主机的物理内存已经无法容纳
Redis的写请求并发量大，一个Redis实例以无法承载

当上述两个问题出现时，就必须要对Redis进行分片了。

Redis的分片方案有很多种，例如很多Redis的客户端都自行实现了分片功能，也有向Twemproxy这样的以代理方式实现的Redis分片方案。然而首选的方案还应该是Redis官方在3.0版本中推出的Redis Cluster分片方案。

本文不会对Redis Cluster的具体安装和部署细节进行介绍，重点介绍Redis Cluster带来的好处与弊端。

Redis Cluster的能力

能够自动将数据分散在多个节点上
当访问的key不在当前分片上时，能够自动将请求转发至正确的分片
当集群中部分节点失效时仍能提供服务

其中第三点是基于主从复制来实现的，Redis Cluster的每个数据分片都采用了主从复制的结构，原理和前文所述的主从复制完全一致，唯一的区别是省去了Redis Sentinel这一额外的组件，由Redis Cluster负责进行一个分片内部的节点监控和自动failover。

Redis Cluster分片原理

Redis Cluster中共有16384个hash slot，Redis会计算每个key的CRC16，将结果与16384取模，来决定该key存储在哪一个hash slot中，同时需要指定Redis Cluster中每个数据分片负责的Slot数。Slot的分配在任何时间点都可以进行重新分配。

客户端在对key进行读写操作时，可以连接Cluster中的任意一个分片，如果操作的key不在此分片负责的Slot范围内，Redis Cluster会自动将请求重定向到正确的分片上。

hash tags

在基础的分片原则上，Redis还支持hash tags功能，以hash tags要求的格式明明的key，将会确保进入同一个Slot中。例如：{uiv}user:1000和{uiv}user:1001拥有同样的hash tag {uiv}，会保存在同一个Slot中。

使用Redis Cluster时，pipelining、事务和LUA Script功能涉及的key必须在同一个数据分片上，否则将会返回错误。如要在Redis Cluster中使用上述功能，就必须通过hash tags来确保一个pipeline或一个事务中操作的所有key都位于同一个Slot中。

有一些客户端（如Redisson）实现了集群化的pipelining操作，可以自动将一个pipeline里的命令按key所在的分片进行分组，分别发到不同的分片上执行。但是Redis不支持跨分片的事务，事务和LUA Script还是必须遵循所有key在一个分片上的规则要求。
主从复制 vs 集群分片

在设计软件架构时，要如何在主从复制和集群分片两种部署方案中取舍呢？

从各个方面看，Redis Cluster都是优于主从复制的方案

Redis Cluster能够解决单节点上数据量过大的问题
Redis Cluster能够解决单节点访问压力过大的问题
Redis Cluster包含了主从复制的能力

那是不是代表Redis Cluster永远是优于主从复制的选择呢？

并不是。

软件架构永远不是越复杂越好，复杂的架构在带来显著好处的同时，一定也会带来相应的弊端。采用Redis Cluster的弊端包括：

维护难度增加。在使用Redis Cluster时，需要维护的Redis实例数倍增，需要监控的主机数量也相应增加，数据备份/持久化的复杂度也会增加。同时在进行分片的增减操作时，还需要进行reshard操作，远比主从模式下增加一个Slave的复杂度要高。
客户端资源消耗增加。当客户端使用连接池时，需要为每一个数据分片维护一个连接池，客户端同时需要保持的连接数成倍增多，加大了客户端本身和操作系统资源的消耗。
性能优化难度增加。你可能需要在多个分片上查看Slow Log和Swap日志才能定位性能问题。
事务和LUA Script的使用成本增加。在Redis Cluster中使用事务和LUA Script特性有严格的限制条件，事务和Script中操作的key必须位于同一个分片上，这就使得在开发时必须对相应场景下涉及的key进行额外的规划和规范要求。如果应用的场景中大量涉及事务和Script的使用，如何在保证这两个功能的正常运作前提下把数据平均分到多个数据分片中就会成为难点。

所以说，在主从复制和集群分片两个方案中做出选择时，应该从应用软件的功能特性、数据和访问量级、未来发展规划等方面综合考虑，只在确实有必要引入数据分片时再使用Redis Cluster。

下面是一些建议：

需要在Redis中存储的数据有多大？未来2年内可能发展为多大？这些数据是否都需要长期保存？是否可以使用LRU算法进行非热点数据的淘汰？综合考虑前面几个因素，评估出Redis需要使用的物理内存。
用于部署Redis的主机物理内存有多大？有多少可以分配给Redis使用？对比(1)中的内存需求评估，是否足够用？
Redis面临的并发写压力会有多大？在不使用pipelining时，Redis的写性能可以超过10万次/秒（更多的benchmark可以参考 https://redis.io/topics/benchmarks ）
在使用Redis时，是否会使用到pipelining和事务功能？使用的场景多不多？

综合上面几点考虑，如果单台主机的可用物理内存完全足以支撑对Redis的容量需求，且Redis面临的并发写压力距离Benchmark值还尚有距离，建议采用主从复制的架构，可以省去很多不必要的麻烦。同时，如果应用中大量使用pipelining和事务，也建议尽可能选择主从复制架构，可以减少设计和开发时的复杂度。
