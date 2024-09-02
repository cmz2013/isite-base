package org.isite.shop.controller;

import org.isite.commons.web.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TradeOrderController extends BaseController {

    /**
     * 支付完成回调API，转发数据到MQ。
     *
     * 消费者完成以下操作：TODO
     * 1）更新订单状态。
     * 2）根据SpuType广播MQ消息。
     * 3）AOP形式发送运营消息。
     */
}
