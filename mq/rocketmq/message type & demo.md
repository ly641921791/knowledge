RocketMQ 消息类型 & 使用示例
-

### 消息类型

RocketMQ 消息类型有以下几种：

1. 无序消息
2. 有序消息
3. 

### 使用示例

#### 无序消息

- 生产者

```java
public class Producer {
    public static void main(String[] args){
		DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
		producer.setNamesrvAddr("127.0.0.1:9876;127.0.0.2:9876");
		producer.start();
		
		Message message = new Message("topic","tag","msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
		SendResult sendResult = producer.send(message);
		
		producer.shutdown();
    }
}
```

- 消费者

```java
public class Consumer {
    public static void main(String[] args){
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup");
        producer.setNamesrvAddr("127.0.0.1:9876;127.0.0.2:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic", "tag");
		consumer.registerMessageListener(new MessageListenerConcurrently() {
		    @Override
		    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,ConsumeConcurrentlyContext context) {
		        System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
		        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		    }
		});
		consumer.start();
    }
}
```

#### 有序消息

#### 

使用


https://www.jianshu.com/p/824066d70da8