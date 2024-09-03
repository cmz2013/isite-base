package org.isite.shop.controller;

import org.isite.commons.web.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.constants.UrlConstants.URL_API;
import static org.isite.shop.support.constants.UrlConstants.URL_SHOP;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TradeOrderController extends BaseController {

    /**
     * @Description 在用户支付成功后，该接口用于接收支付系统的通知，并转发到MQ（敏感数据，处理失败时可以重试）
     */
    @PostMapping(URL_API + URL_SHOP + "/pay/notify")
    public String payNotify(@RequestBody String data) {
        // TODO 解析数据，校验签名合法且支付成功时(设计策略模式，支持多种支付方式)，转发数据（多种支付方式统一数据结构）到MQ: QUEUE_TRADE_ORDER_SUCCESS
        return null;
    }
}
