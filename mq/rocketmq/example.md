RocketMQ 使用示例
-

### 相关了解

查看示例之前需要了解以下概念：

1. 消息类型
2. 发送方式
3. 消费模式

#### 消息类型

RocketMQ 消息类型有以下几种：

1. 无序消息
2. 有序消息

消息的发送和接收通过队列实现，若只使用一条队列，就可以保证全局消息有序，但是效率低下，可以使用多个队列，按照某种规则指定消息传递的队列（例如用户id），这样就
可以保证局部消息有序

3. 延时消息

broker接收到消息后，延时发送，例如：订单超时关闭

#### 发送方式

RocketMQ 支持以下3种发送方式，特点如下：

|发送方式|TPS|响应结果|可靠性|
|---|---|---|---|
|同步发送|小|有|可靠|
|异步发送|中|有|可靠|
|单向发送|大|无|可能丢失|

#### 消费模式

RocketMQ是基于发布订阅模型的消息中间件，消息消费失败会进行消费重投，默认16次，重试间隔随重试次数提高，最后进入死信队列

1. 集群消费

groupName、topic、tag相同的consumer形成集群，同一消息只会被集群中的一个消费者消费

失败的消息不保证重新投递到同一consumer实例

2. 广播消费

集群中每个consumer都会收到消息，消息会被重复消费，不会进行消息重投，需要关注处理消费失败的情况

3. 集群 & 广播

不同group的consumer订阅同一topic实现该消费模式

### 使用示例

#### 简单消息

- 发送同步消息

```java
public class Producer {
    public static void main(String[] args){
		DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
		producer.setNamesrvAddr("127.0.0.1:9876;127.0.0.2:9876");
		producer.start();
		
		Message message = new Message("topic","tag","msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
		// 调用同步发送方法
		SendResult sendResult = producer.send(message);
		
		producer.shutdown();
    }
}
```

- 发送异步消息

```java
public class Producer {
    public static void main(String[] args){
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        
        // 设置异步消息失败重试次数
        producer.setRetryTimesWhenSendAsyncFailed(0);
        
        Message message = new Message("topic1","tag1","key1","msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 调用异步发送方法
        producer.send(message,new SendCallback(){
	        @Override
	        public void onSuccess(SendResult sendResult) {
	            
	        }
	        @Override
	        public void onException(Throwable e) {
	            
	        }
        });
        
        producer.shutdown();
    }
}
```

- 发送单向消息

```java
public class Producer {
    public static void main(String[] args){
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        
        Message message = new Message("topic1","tag1","msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 调用单向消息发送方法
        producer.sendOneway(message);
        
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

- 生产者

```java
public class Producer {
    public static void main(String[] args){
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        
        Long userId = 1L;
        Message message = new Message("topic1","tag1","key1","msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 选择队列发送
        SendResult sendResult = message.send(message,new MessageQueueSelector(){
	        @Override
	        public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
	            Long id = (Long) arg;
	            int index = id % mqs.size();
	            return mqs.get(index);
	        }
        },userId);
        
        producer.shutdown();
    }
}
```

- 消费者

```java
public class Consumer {
    public static void main(String[] args){
        DefaultMQPushConsumer consumer = new DefaultMQPullConsumer("consumerGroup");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic1", "tag1");
        
        // 使用有序消息监听器
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        
        consumer.start();
    }
}
```

#### 延时消息

RocketMQ不支持自定义延时时间，通过设置消息属性`延时级别`实现延时消息：`public void setDelayTimeLevel(int level)`

#### 批量消息

通过调用批量消息重载方法即可：`public SendResult send(Collection<Message> msgs)`，尽可能保证每次总大小不超过4MB

#### 消息过滤

1. 过滤标签

消费者可以设置接收的标签

```java
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup");
// 表示接收 全部标签的消息
consumer.subscribe("topic1", "*");
// 表示接收 tag1 、 tag2 、 tag3三个标签的消息
consumer.subscribe("topic1", "tag1 || tag2 || tag3");
```

2. 过滤属性

首先设置消息属性 a=1

```java
Message msg = new Message("topic1","tag1","msg".getBytes(RemotingHelper.DEFAULT_CHARSET));
// 设置消息属性 a=1
msg.putUserProperty("a", "1");
```

消费者设置过滤器

```java
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup");
// 只有订阅的消息有这个属性a, a >=0 and a <= 3
consumer.subscribe("topic1", MessageSelector.bySql("a between 0 and 3");
```

支持多种SQL语法，此处略

#### 其他类型

事物消息、OMS消息略
