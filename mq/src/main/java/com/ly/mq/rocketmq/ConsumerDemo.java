package com.ly.mq.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author ly
 * @since 2019-03-06 17:30
 **/
public class ConsumerDemo {

    public static void main(String[] args) throws Exception {
        // 创建消费者

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup");

        // 设置NameServer，多个地址";"隔开
        consumer.setNamesrvAddr("192.168.1.104:9876");

        // 设置从第一条消息开始消费，
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 设置订阅的topic和tag
        consumer.subscribe("topic1", "tag1");

        // 设置消息处理逻辑
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息：" + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 启动
        consumer.start();

        // 停止
        consumer.shutdown();
    }

}
