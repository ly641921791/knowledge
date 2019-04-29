顺序消息
-

##### 使用示例

RocketMQ通过将队列选择器将消息发送到同一个队列，保证理论上的消息有序，实际应用中，由于网络或其他原因，可能导致消息消费无序

###### 生产者

```java
public class OrderedProducer {
    public static void main(String[] args){
		MQProducer producer = new DefaultMQProducer("group_name");
		producer.setNamesrvAddr("127.0.0.1:9876");
		producer.start();
		
		Message message = new Message("topic","tag","key","message".getBytes(RemotingHelper.DEFAULT_CHARSET));
		
		// 顺序消息是通过消息发送到同一个消息队列保证顺序的，此处通过用户id计算获得消息队列
		Long userId = 1L;
		
		SendResult result = producer.send(message,new MessageQueueSelector(){
		    @Override
		    public MessageQueue select(List<MessageQueue> mq, Message msg,Object arg){
		        Long id = (Long) arg;
		        int index = id % mq.size();
		        return mq.get(index);
		    }
		},userId);
		
		producer.shutdown();
    }
}
```

###### 消费者

```java
public class OrderedConsumer {
    public static void main(String[] args){
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_name");
		consumer.setNamesrvAddr("127.0.0.1:9876");
		consumer.setConsumerFromWhere(ConsumeFromWhere.CONSUMER_FROM_LAST_OFFSET);
		consumer.subscribe("topic","tag");
		consumer.start();
		
		// 有序消息监听器
		consumer.registerMessageListener(new MessageListenerOrderly(){
		    @Override
		    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> ms,ConsumeOrderlyContext context){
		        return ConsumeOrderlyStatus.SUCCESS;
		    }
		});
		
		consumer.start();
    }
}
```
