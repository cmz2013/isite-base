package org.isite.commons.web.mq;

/**
 * @Description 消费者接口：完成消息消费，进行手动确认
 * 自动确认是 RabbitMQ 默认的消息确认。RabbitMQ成功将消息发出（即将消息成功写入TCP Socket中），自动确认立即认为本次投递已经被正确处理，
 * 不管消费端是否成功处理本次投递。这种情况如果消费端消费逻辑抛出异常，也就是消费端没有处理成功这条消息，那么就相当于丢失了消息。
 * 手动确认 AcknowledgeMode.MANUAL 是多数选择的模式。消费者收到消息后，手动调用 basic.ack/basic.nack/basic.reject，
 * RabbitMQ 收到这些消息后，才认为本次投递成功。
 * @Author <font color='blue'>zhangcm</font>
 */
public interface Consumer<M> {
    /**
     * 消息消费
     * @param message 消息数据
     * @return 消息确认。由业务控制是否返回队列重试
     */
    Basic handle(M message);
}
