package org.isite.commons.web.mq;

import com.rabbitmq.client.Channel;

import java.io.IOException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * @Description 消息手动模式确认接口。消费者收到消息后，手动调用 basic.ack/basic.nack/basic.reject，RabbitMQ收到这些消息后，才认为本次投递成功。
 * 消费端以上的3个方法都表示消息已经被正确投递，但是 basic.ack表示消息已经被正确处理;而 basic.nack 和 basic.reject表示没有被正确处理。
 * basic.nack 是 basic.reject协议的扩展，basic.reject一次只能拒绝单条消息
 * basic.nack 一次可以拒绝多条消息，即当前通道消息的 deliveryTag 小于当前这条消息的，都拒绝确认
 * @Author <font color='blue'>zhangcm</font>
 */
public interface Basic {
    /**
     * 进行消息确认。使用拒绝后重新入列这个确认模式要谨慎(eg：实现分布式事务)，
     * 如果使用不当会导致一些每次都被你重入列的消息一直消费-入列-消费-入列这样循环，会导致消息积压。
     *
     * @param deliveryTag 用来标识信道中投递的消息。RabbitMQ 推送消息给Consumer时，会附带一个deliveryTag，以便Consumer可以
     * 在消息确认时告诉RabbitMQ到底是哪条消息被确认了。RabbitMQ保证在每个信道中，每条消息的deliveryTag从1开始递增
     *
     * @param channel 信道。消息确认操作的作用范围是当前信道
     *
     * @throws IOException 不能进行消息确认，消费者需要注意防止消息被重复消费
     * 1) connection closed: 如果消费超时（如果RabbitMQ不配置，默认是3分钟），信道会关闭消息将无法进行确认，返回队列重新推送
     * 2) unknown delivery tag: 不能重复确认，或者确认recover以前的消息。recover以后，该信道待确认的消息被删除，重新推送
     */
    void confirm(long deliveryTag, Channel channel) throws IOException;

    class Ack implements Basic {
        /**
         * 传入false只确认当前一个消息收到，true确认所有consumer获得的消息
         */
        private boolean multiple = FALSE;

        public Ack setMultiple(boolean multiple) {
            this.multiple = multiple;
            return this;
        }

        /**
         * 成功消费，消息从队列中删除
         */
        @Override
        public void confirm(long deliveryTag, Channel channel) throws IOException {
            channel.basicAck(deliveryTag, multiple);
        }
    }

    class Nack implements Basic {
        /**
         * 传入true，就是将数据重新丢回队列里，那么下次还会消费这消息。
         * 传入false，就是告诉服务器，我已经知道这条消息数据了，因为一些原因拒绝它，而且服务器也把这个消息丢掉就行
         */
        private boolean requeue = FALSE;
        /**
         * 是否针对多条消息
         * multiple=true: 消息id<=deliveryTag的消息，都会被确认
         * multiple=false: 消息id=deliveryTag的消息，都会被确认
         */
        private boolean multiple = FALSE;

        public Nack setRequeue(boolean requeue) {
            this.requeue = requeue;
            return this;
        }

        public Nack setMultiple(boolean multiple) {
            this.multiple = multiple;
            return this;
        }

        @Override
        public void confirm(long deliveryTag, Channel channel) throws IOException {
            channel.basicNack(deliveryTag, multiple, requeue);
        }
    }

    /**
     * channel.basicRecover 方法用来请求 RabbitMQ 重新发送还未被确认的消息。
     * 如果之前未确认的消息消费成功后再进行消息确认，会出现unknown delivery tag
     * basicRecover使用不当，会导致消息被重复消费
     */
    class Recover implements Basic {
        /**
         * 如果 requeue 参数设置为 true，则未被确认的消息会被重新加入到队列中，这样对于同一条消息来说，
         * 可能会被分配给与之前不同的消费者。如果 requeue 参数设置为 false，那么同一条消息会被分配给与之前相同的消费者。
         */
        private boolean requeue = TRUE;

        public Recover setRequeue(boolean requeue) {
            this.requeue = requeue;
            return this;
        }

        @Override
        public void confirm(long deliveryTag, Channel channel) throws IOException {
            channel.basicRecover(requeue);
        }
    }
}

