RocketMQ
-

### RocketMQ 是什么

RocketMQ 是低延迟、高可靠、可伸缩、易使用的消息中间件。具体以下特性：

1. 支持发布/订阅（Pub/Sub）和点对点（P2P）消息模型
2. 支持先进先出（FIFO）的消息队列
3. 支持拉（pull）和推（push）消息模式
4. 单一队列百万消息的堆积能力
5. 支持多种协议，如：JMS、MQTT等
6. 分布式高可用架构

### 专业术语

#### Producer

消息生产者，作用是将消息发送到MQ

#### Producer Group

生产者组，多个发送同一类型消息的生产者

#### Consumer

消息消费者，作用是接收MQ上的消息

#### Consumer Group

同Producer Group

#### Topic

消息的逻辑分类，如：订单消息、库存消息

#### Tag

对同一Topic下消息的细化，标记不同用途

#### Message

消息载体，必须指定topic

#### Broker

接收、存储、推拉消息

#### Name Server

轻量级的服务发现和路由

### RocketMQ 架构

![RocketMQ 架构图](rocketmq.png)	

Broker的主从节点会进行数据同步，每个Broker会和NameServer的所有节点建立长连接，定时注册Topic信息

Producer与NameServer的随机节点建立长连接，定期获取Topic信息，并与Broker主节点建立长连接，定时发送心跳

Consumer同时和Broker的主从节点建立长连接

### RockerMQ 集群部署模式

1. 一主

适合个人学习

2. 多主

优点：所有模式中性能最高
缺点：单master节点宕机期间，未消费消息不可用，实时性收到影响

3. 多主多从异步复制

主节点可读可写，从节点可读，类似MySQL的主备
优点：解决多主消息实时性问题
缺点：使用异步复制的同步方式有可能会有消息丢失的问题

4. 多主多从同步复制

优点：同步双写的同步模式能保证数据不丢失
缺点：消息的RT（response time）略长，性能低10%左右


##### FAQ

###### 消息重复

消息重复的问题若由消息系统解决，会影响系统的吞吐量和高可用。因此，RocketMQ不保证消息不重复，需要自己进行消息去重，如何解决这个问题？

1. 通过数据库、Redis或其他存储中间件记录已消费消息
2. 消费端实现处理逻辑保证幂等性



> 阿里中间件团队相关文章 http://jm.taobao.org/2017/01/12/rocketmq-quick-start-in-10-minutes/