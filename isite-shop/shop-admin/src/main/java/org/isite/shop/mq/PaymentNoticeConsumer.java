package org.isite.shop.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.TRUE;

/**
 * @Description 消费者完成以下操作：
 * 1）更新订单状态。
 * 2）根据Spu供应商广播MQ消息。
 * 3）AOP形式发送运营消息。
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class PaymentNoticeConsumer implements Consumer<Object> {

    @Override
    public Basic handle(Object message) {
        try {
            //TODO

            return new Basic.Ack();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new Basic.Nack().setRequeue(TRUE);
        }
    }
}
