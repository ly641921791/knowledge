[TOC]

# 分布式事务



事物
	单体事物    ACID
		Atomicity   原子性 ：事物是一个整体
		Consistency 一致性 ：事物前后一致，若失败则回到原始状态
		Isolation   隔离线 ：不同事物操作互不影响
		Durability  持久性 ：事物成功提交，状态被记录以来
	分布式事物   CAP （布鲁尔定理）
		Consistency         一致性 ：读操作可以返回最新的数据。
						强一致性 ：任意时刻，任意节点数据都是一样的
						弱一致性 ：虽然不能保证强一致性，但是所有节点的数据会达到最终一致性
		Availability        可用性 ：非故障节点应该在合理时间内返回合理相应
		Partition tolerance 分区容错性 ：若某个节点出现问题，整个集群还可以正常工作

BASE理论 ：Basically Available(基本可用)、Soft state(软状态)和 Eventually consistent (最终一致性)三个短语的缩写，是对 CAP 中 AP 的一个扩展。
	
	基本可用：分布式系统在出现故障时，允许损失部分可用功能，保证核心功能可用。
	软状态：允许系统中存在中间状态，这个状态不影响系统可用性，这里指的是 CAP 中的不一致。
	最终一致：最终一致是指经过一段时间后，所有节点数据都将会达到一致。
		
BASE 解决了 CAP 中理论没有网络延迟，在 BASE 中用软状态和最终一致，保证了延迟后的一致性。
BASE 和 ACID 是相反的，它完全不同于 ACID 的强一致性模型，而是通过牺牲强一致性来获得可用性，并允许数据在一段时间内是不一致的，但最终达到一致状态。



分布式事物常见解决方案

	2PC（Two Phase Commitment Protocol）
	3PC（CanCommit、PreCommit、DoCommit）
	
2PC ：两段提交协议，XA transaction

	优点 ：保证强一致性，实现成本低。
	缺点 ：
		单点问题 ：若二段提交事物管理器宕机，会导致资源被阻塞，数据库不可用
		同步阻塞 ：二段提交时会阻塞到提交结束
		数据不一致 ：若因为网络原因，二段提交后，部分事物参数者未收到Commit通知，则阻塞且数据不一致

TCC ：Try-Confirm-Cancel

	TCC 事务机制相比于上面介绍的 XA，解决了如下几个缺点：
	解决了协调者单点，由主业务方发起并完成这个业务活动。业务活动管理器也变成多点，引入集群。
	同步阻塞：引入超时，超时后进行补偿，并且不会锁定整个资源，将资源转换为业务逻辑形式，粒度变小。
	数据一致性，有了补偿机制之后，由业务活动管理器控制一致性。

![](https://mmbiz.qpic.cn/mmbiz_png/WLIGprPy3z6QR2ahbaxn60XjuXS3XNrL8JiaQCljO1cq47hwuR9eNZib5J83o6SCtIGST9GHgGhXIbePs1lfX3Qg/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

	Try 阶段：尝试执行，完成所有业务检查（一致性），预留必需业务资源（准隔离性）。
	Confirm 阶段：确认真正执行业务，不作任何业务检查，只使用 Try 阶段预留的业务资源，Confirm 操作满足幂等性。要求具备幂等设计，Confirm 失败后需要进行重试。
	Cancel 阶段：取消执行，释放 Try 阶段预留的业务资源，Cancel 操作满足幂等性。Cancel 阶段的异常和 Confirm 阶段异常处理方案基本上一致。







举个简单的例子：如果你用 100 元买了一瓶水， Try 阶段：你需要向你的钱包检查是否够 100 元并锁住这 100 元，水也是一样的。

如果有一个失败，则进行 Cancel(释放这 100 元和这一瓶水)，如果 Cancel 失败不论什么失败都进行重试 Cancel，所以需要保持幂等。

如果都成功，则进行 Confirm，确认这 100 元被扣，和这一瓶水被卖，如果 Confirm 失败无论什么失败则重试(会依靠活动日志进行重试)。

对于 TCC 来说适合一些：
强隔离性，严格一致性要求的活动业务。
执行时间较短的业务。

实现参考：https://github.com/liuyangming/ByteTCC/。

  本地消息表

本地消息表这个方案最初是 eBay 提出的，eBay 的完整方案 https://queue.acm.org/detail.cfm?id=1394128。

此方案的核心是将需要分布式处理的任务通过消息日志的方式来异步执行。消息日志可以存储到本地文本、数据库或消息队列，再通过业务规则自动或人工发起重试。

人工重试更多的是应用于支付场景，通过对账系统对事后问题的处理。 
![](https://mmbiz.qpic.cn/mmbiz_png/WLIGprPy3z6QR2ahbaxn60XjuXS3XNrL8w0nib0Hej1ys7mfibcjKjyYFN0MxyY12nUFLt8Lm11xZI2VuDVbCUXg/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)
对于本地消息队列来说核心是把大事务转变为小事务。还是举上面用 100 元去买一瓶水的例子。

1. 当你扣钱的时候，你需要在你扣钱的服务器上新增加一个本地消息表，你需要把你扣钱和减去水的库存写入到本地消息表，放入同一个事务(依靠数据库本地事务保证一致性）。

2. 这个时候有个定时任务去轮询这个本地事务表，把没有发送的消息，扔给商品库存服务器，叫它减去水的库存，到达商品服务器之后，这时得先写入这个服务器的事务表，然后进行扣减，扣减成功后，更新事务表中的状态。

3. 商品服务器通过定时任务扫描消息表或者直接通知扣钱服务器，扣钱服务器在本地消息表进行状态更新。

4. 针对一些异常情况，定时扫描未成功处理的消息，进行重新发送，在商品服务器接到消息之后，首先判断是否是重复的。

如果已经接收，再判断是否执行，如果执行在马上又进行通知事务；如果未执行，需要重新执行由业务保证幂等，也就是不会多扣一瓶水。

本地消息队列是 BASE 理论，是最终一致模型，适用于对一致性要求不高的情况。实现这个模型时需要注意重试的幂等。
  MQ 事务

在 RocketMQ 中实现了分布式事务，实际上是对本地消息表的一个封装，将本地消息表移动到了 MQ 内部。

下面简单介绍一下MQ事务，如果想对其详细了解可以参考：https://www.jianshu.com/p/453c6e7ff81c。 
![](https://mmbiz.qpic.cn/mmbiz_png/WLIGprPy3z6QR2ahbaxn60XjuXS3XNrLCcFeibsiawPb8IP5N1sVdGp8UJbUcEJpr09w4C7Q6qyrAkx2qNB3mKOA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)
基本流程如下：
第一阶段 Prepared 消息，会拿到消息的地址。
第二阶段执行本地事务。
第三阶段通过第一阶段拿到的地址去访问消息，并修改状态。消息接受者就能使用这个消息。

如果确认消息失败，在 RocketMQ Broker 中提供了定时扫描没有更新状态的消息。

如果有消息没有得到确认，会向消息发送者发送消息，来判断是否提交，在 RocketMQ 中是以 Listener 的形式给发送者，用来处理。 
![](https://mmbiz.qpic.cn/mmbiz_png/WLIGprPy3z6QR2ahbaxn60XjuXS3XNrLOfOQg3tt68VTvgALGu5ERmeeyic0vibbyEgk11WbAtFc6YaOvGvQyZ0w/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)
如果消费超时，则需要一直重试，消息接收端需要保证幂等。如果消息消费失败，这时就需要人工进行处理，因为这个概率较低，如果为了这种小概率时间而设计这个复杂的流程反而得不偿失。

  Saga 事务

Saga 是 30 年前一篇数据库伦理提到的一个概念。其核心思想是将长事务拆分为多个本地短事务，由 Saga 事务协调器协调，如果正常结束那就正常完成，如果某个步骤失败，则根据相反顺序一次调用补偿操作。

Saga 的组成：每个 Saga 由一系列 sub-transaction Ti 组成，每个 Ti 都有对应的补偿动作 Ci，补偿动作用于撤销 Ti 造成的结果。这里的每个 T，都是一个本地事务。

可以看到，和 TCC 相比，Saga 没有“预留 try”动作，它的 Ti 就是直接提交到库。

Saga 的执行顺序有两种：
T1，T2，T3，...，Tn。
T1，T2，...，Tj，Cj，...，C2，C1，其中 0 < j < n 。

Saga 定义了两种恢复策略：
向后恢复，即上面提到的第二种执行顺序，其中 j 是发生错误的 sub-transaction，这种做法的效果是撤销掉之前所有成功的 sub-transation，使得整个 Saga 的执行结果撤销。
向前恢复，适用于必须要成功的场景，执行顺序是类似于这样的：T1，T2，...，Tj(失败)，Tj(重试)，...，Tn，其中 j 是发生错误的 sub-transaction。该情况下不需要 Ci。

这里要注意的是，在 Saga 模式中不能保证隔离性，因为没有锁住资源，其他事务依然可以覆盖或者影响当前事务。

还是拿 100 元买一瓶水的例子来说，这里定义：
T1 = 扣 100 元，T2 = 给用户加一瓶水，T3 = 减库存一瓶水。
C1 = 加100元，C2 = 给用户减一瓶水，C3 = 给库存加一瓶水。

我们一次进行 T1，T2，T3 如果发生问题，就执行发生问题的 C 操作的反向。

上面说到的隔离性的问题会出现在，如果执行到 T3 这个时候需要执行回滚，但是这个用户已经把水喝了(另外一个事务)，回滚的时候就会发现，无法给用户减一瓶水了。

这就是事务之间没有隔离性的问题。可以看见 Saga 模式没有隔离性的影响还是较大，可以参照华为的解决方案：从业务层面入手加入一 Session 以及锁的机制来保证能够串行化操作资源。

也可以在业务层面通过预先冻结资金的方式隔离这部分资源， 最后在业务操作的过程中可以通过及时读取当前状态的方式获取到最新的更新。（具体实例：可以参考华为的 Service Comb）
5 总结

还是那句话，能不用分布式事务就不用，如果非得使用的话，结合自己的业务分析，看看自己的业务比较适合哪一种，是在乎强一致，还是最终一致即可。

最后在总结一些问题，大家可以下来自己从文章找寻答案：
ACID 和 CAP 的 CA 是一样的吗？
分布式事务常用的解决方案的优缺点是什么？适用于什么场景？
分布式事务出现的原因？用来解决什么痛点？



























MySql InnoDB事物原理
	MySql的InnoDB的事物通过InnoDB日志和锁实现。隔离性通过锁实现，持久性通过Redo Log实现，原子性和一致性通过Undo Log实现。
	操作数据之前，将数据备份到Undo Log中，然后操作数据，若回滚则利用Undo Log中的数据恢复，Redo Log记录新数据，提交事物即将Redo Log持久化




消息中间件在分布式系统中的主要作用：
	异步通讯
	解耦
	并发缓冲


XA协议：两段提交协议

如何保证消息一致性（消息发送存在失败的可能）
	1.发送到消息中间件，待发送状态
	2.执行业务，若成功，则发送状态
	3.将操作结果发送中间件
	
	
可靠消息最终一致性
	1.本地消息服务（同一个事物中），异常消息通过消息恢复系统处理。并通过回调或确认消息队列的方式确认消息。
	2.独立消息服务，通过消息服务存储消息，通过收到业务成功消息才发送消息
	
	
	
	
	
	
	













消息服务表
	
	id                  uuid
	message_id
	message_body        消息内容
	message_data_type   消息数据类型(json)
	consumer_queue      消息队列
	message_send_times  重发次数
	already_dead        是否死亡
	status              状态
	remark              备注
	version     
	create_name         
	update_name
	create_time
	update_time

消息服务接口

```java
public interface MessageService{
	
	/**
	* 预存消息
	*/
	int saveMessageWaitingConfirm(Message message) throws Exception;
	
	/**
	* 确认并发送消息
	*/
	void confirmAndSendMessage(int messageId) throws Exception;
	
	/**
	* 存储并发送
	*/
	int saveAndSendMessage(Message message) throws Exception;
	
	/**
	* 直接发送
	*/
	void directSendMessage(Message message) throws Exception;
	
	// 重发消息
	void resendMessage(Message message) throws Exception;
	
	// 根据messageId重发
	
	// 标记消息死亡
	
	// 根据messageId查找消息
	
	// 删除消息（成功或失败后）
	
	// 重发消息队列的死亡消息
	
	// 批量获取消息
	
	// 处理等待超时的消息
	handleWaitingConfirmTimeOutMessage();
}
```
