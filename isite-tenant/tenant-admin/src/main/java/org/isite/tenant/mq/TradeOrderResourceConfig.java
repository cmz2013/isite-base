package org.isite.tenant.mq;

import org.isite.shop.support.mq.TradeOrderExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.isite.shop.support.enums.SpuType.TENANT_RESOURCE;
import static org.isite.tenant.data.constants.TenantConstants.QUEUE_TRADE_ORDER_SUCCESS_TENANT_RESOURCE;
import static org.springframework.amqp.core.BindingBuilder.bind;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class TradeOrderResourceConfig {

    @Bean
    public Queue tradeOrderResourceQueue() {
        /*
         * durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
         * exclusive 是否为排他队列，true表示排他队列，一个排他队列只有声明这个队列的连接能够使用。 注意：
         * 1、其他的连接不能使用排他队列，同时也不能再去声明一个队列名相同的队列
         * 2、排他队列是和创建的连接绑定的，连接声明的多个Channel是可以共享排他队列的
         * 3、排他队列不能设置为持久化，一旦创建排他队列的连接关闭，排他队列自动删除
         *
         * autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除
         */
        return new Queue(QUEUE_TRADE_ORDER_SUCCESS_TENANT_RESOURCE, true, false, false);
    }

    @Bean
    public Binding resourceSpuBinding(TradeOrderExchange tradeOrderExchange) {
        return bind(tradeOrderResourceQueue()).to(tradeOrderExchange).with(TENANT_RESOURCE.name());
    }
}
