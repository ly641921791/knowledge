部署模式
-

Redis部署模式有下面几种

- 单节点模式 Singleton
- 主从模式 Master/Slave
- 哨兵模式 Sentinel
- 集群模式 Cluster

###### 单节点模式

单节点模式适用于学习

###### 主从模式

作用

1. 备份数据
2. 负载均衡，读写分离

注意

1. slave默认是readonly。设置可写没意义，因为不会同步到其他节点，master同步数据时会覆盖
2. master挂了以后，slave不会竞选成为master

缺点

master挂了会导致不能进行写操作

###### 哨兵模式

1. Master挂了，Sentinel会从Slave中重新选择Master。并修改所有节点的配置文件，如：slaveof指向master
2. 旧Master会作为Slave重启
3. Sentinel也可能挂掉，因此Sentinel也需要集群
4. Sentinel可以管理多个主从Redis
5. Sentinel避免和Redis部署在一起，导致同时挂了
6. 使用Sentinel模式，客户端连接Sentinel，通过提供可用的Redis实现

###### 集群模式

为了解决单机Redis容量的问题


