# Redis

## 简介与优势

Redis是高性能的K-V的内存数据库，每秒可以处理10万次读写操作，是已知性能最快的K-V DB。

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
	
