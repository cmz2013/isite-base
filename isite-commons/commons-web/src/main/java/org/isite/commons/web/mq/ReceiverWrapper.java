package org.isite.commons.web.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

/**
 * @Description MQ消息接收者 Wrapper，调用消费者完成消费，发送消息回执进行确认。
 * 消费端实现约定接口，遵循消费端程序规范
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class ReceiverWrapper<B> {
    /**
     * 消费者接口
     */
    private final Consumer<B> consumer;

    public ReceiverWrapper(Consumer<B> consumer) {
        this.consumer = consumer;
    }

    /**
     * @Description 处理MQ消息并进行确认
     */
    @RabbitHandler
    public void handle(Message message, @Payload B body, Channel channel) throws IOException {
        Basic basic = null;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            basic = consumer.handle(body);
        } catch (Exception e) {
            log.error("mq message error", e);
            //防止MQ队列消息消费异常导致积压，默认丢弃消息
            basic = new Basic.Nack();
        }
        basic.confirm(deliveryTag, channel);
    }
}
