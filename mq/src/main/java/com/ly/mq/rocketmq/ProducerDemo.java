package com.ly.mq.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author ly
 * @since 2019-03-06 17:19
 **/
public class ProducerDemo {

    public static void main(String[] args) throws Exception {
        // 创建Producer
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");

        // 设置NameServer地址，多个地址用";"隔开
        producer.setNamesrvAddr("192.168.1.104:9876");

        // 启动
        producer.start();

        // 创建消息
        Message message = new Message("topic1", "tag1", "消息1".getBytes(RemotingHelper.DEFAULT_CHARSET));

        // 发送消息
        SendResult sendResult = producer.send(message);
        System.out.println("发送结果：" + sendResult);

        // 停止
        producer.shutdown();
    }

}
