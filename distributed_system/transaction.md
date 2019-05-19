分布式事务
-

解决方案

- 刚性事务
	- 全局事务XA
- 柔性事务
	- 基于可靠消息的最终一致性方案。适用性广
	- TCC事务补偿型方案。订单、资金、积分等处理
	- 最大努力通知行方案。商户通知业务场景
	- 纯补偿型

###### XA

2PC（Two Phase Commit）

通过引入TM（Transaction Manager）管理RM（Resource Manager）事务的2PC

优点：严格的ACID

缺点：效率低下。数据被Lock直到全局事务结束，伸缩性差，只有实现XA协议才支持

###### BASE理论

- BA：Basic Availability 基本业务可用，支持分区失败
- S：Soft state 柔性状态，状态允许短时间不同步
- E：Eventual consistency 最终一致性，不要求实时一致性

当ACID不能保证时，唯有降低一致性C和隔离性I保证原子性A和持久性D

###### 柔性事务

实现类型

- 2PC
- 补偿型
- 异步确保型
- 最大努力通知型 

可查询：操作具有唯一标识，可查询执行结果

幂等性：重复调用多次和调用一次的结果是一样的，1 通过业务操作实现幂等性 2 通过缓存请求和处理结果实现幂等性

TCC操作：

1. Try 业务检查（一致性），预留资源（隔离性）
2. Confirm 使用Try预留的执行处理业务，需要满足幂等性
3. Cancel 释放预留资源，需要满足幂等性


###### 消息一致性

1. 主动方发送消息到消息服务，状态为待确认
2. 消息服务持久化消息，不投递，并将持久化结果返回
3. 消息持久化成功，则主动方继续业务，失败则放弃业务操作。业务完成，发送结果给消息服务
4. 若成功则消息状态改为待发送，失败则删除消息






###### 可靠消息服务设计

现有消息中间件不支持消息暂存待发送状态，确认发送再发送的处理，因此要创建消息服务，通过消息服务实现该处理

- 本地消息服务
- 独立消息服务

消息服务表设计

```mysql
CREATE TABLE message (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
    message_id VARCHAR(128) NOT NULL DEFAULT '' COMMENT '消息id',
    message_body LONGTEXT NOT NULL DEFAULT '' COMMENT '消息内容',
    message_data_type VARCHAR(64) NOT NULL DEFAULT '' COMMENT '消息数据类型',
    message_queue VARCHAR(64) NOT NULL DEFAULT '' COMMENT '消息队列',
    message_send_times TINYINT NOT NULL DEFAULT 0 COMMENT '消息重试次数',
    already_dead TINYINT NOT NULL DEFAULT 0 COMMENT '是否死亡',
    status VARCHAR(32) NOT NULL DEFAULT '' COMMENT '状态',
    remark VARCHAR(64) NOT NULL DEFAULT '' COMMENT '备注',
    version INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '版本号',
    creator VARCHAR(64) NOT NULL DEFAULT '' COMMENT '创建人',
    create_time TIMESTAMP NOT NULL COMMENT '创建时间',
    editor VARCHAR(64) NOT NULL DEFAULT '' COMMENT '修改人',
    edit_time TIMESTAMP NOT NULL COMMENT '修改时间',
    PRIMARY KEY (id)
)
```

消息服务接口设计

```java
public interface RpMessageService {
    
    // 预存消息
    public int saveMessageWaitingConfirm(RpMessage message) throws MessageException;
    
    // 确认并发送消息
    public void confirmAndSendMessage(String messageId) throws MessageException;
    
    // 保存并发送消息
    public void saveAndSendMessage(RpMessage message) throws MessageException;
    
    // 直接发送消息
    public void directSendMessage(RpMessage message) throws MessageException;
 
    // 重发消息
    public void resendMessage(RpMessage message) throws MessageException;
    
    // 根据消息id重发消息
    public void resendMessageById(String messageId) throws MessageException;
    
    // 标记消息死亡
    public void setMessageAlreadyDead(String messageId) throws MessageException;
    
    // 根据消息id获得消息
    public RpMessage getMessageByMessageId(String messageId) throws MessageException;
    
    // 根据消息id删除消息
    public void deleteMessageByMessageId(String messageId) throws MessageException;
    
    // 重发某队列全部死信
    public void resendAllDeadMessageByQueue(String queue,int batchSize) throws MessageException;
    
    // 获得分页数据
    PageBean listPage(PageParam pageParam,Map<String,Object> paramMap) throws MessageException;
    
}
```
