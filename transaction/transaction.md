事物
	ACID
	Atomicity原子性
	Consistency一致性
	Isolation隔离线
	Durability持久性


CAP理论

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
