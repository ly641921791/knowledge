package com.example.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * @author ly
 */
public class RocketApplication {

    /**
     * 同步消息，用于短信或其他场景
     *
     * @throws Exception 异常
     */
    private void sendSync() throws Exception {

        // 创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("group_name");

        // 设置地址
        producer.setNamesrvAddr("localhost:9876");

        // 运行
        producer.start();

        // 创建消息
        Message message = new Message(
                "topic_name",
                "tags_name",
                "message_body".getBytes(RemotingHelper.DEFAULT_CHARSET)
        );

        // 发送消息
        SendResult result = producer.send(message);
        System.out.printf("%s%n", result);

        // 关闭
        producer.shutdown();
    }

    /**
     * 异步消息，用于于响应时间敏捷场景
     *
     * @throws Exception 异常
     */
    private void sendAsync() throws Exception {

        // 创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("group_name");

        // 设置地址
        producer.setNamesrvAddr("localhost:9876");

        // 运行
        producer.start();

        // 创建消息
        Message message = new Message(
                "topic_name",
                "tags_name",
                "keys_name",
                "message_body".getBytes(RemotingHelper.DEFAULT_CHARSET)
        );

        // 发送消息
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable e) {
                System.out.println(e);
            }
        });

        producer.shutdown();

    }


}
