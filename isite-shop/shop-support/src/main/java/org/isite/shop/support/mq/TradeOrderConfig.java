package org.isite.shop.support.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description Spring AMQP提供了声明式配置，可以确保队列和交换机只被创建一次，即使多个微服务实例同时启动。
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class TradeOrderConfig {

    @Bean
    public TradeOrderExchange tradeOrderExchange() {
        return new TradeOrderExchange();
    }
}
