package org.isite.shop.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.isite.commons.cloud.constants.UrlConstants.URL_API;
import static org.isite.commons.lang.data.Result.success;
import static org.isite.shop.support.constants.UrlConstants.URL_SHOP;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TradeOrderController extends BaseController {

    /**
     * @Description 接收微信支付通知，并转发到MQ（敏感数据，处理失败时可以重试）
     */
    @PostMapping(URL_API + URL_SHOP + "/wechat/notify")
    public String wechatNotify(@RequestBody String data) {
        // TODO 解析数据，校验签名合法且支付成功时，转发数据（多种支付方式统一数据结构）到MQ: QUEUE_TRADE_ORDER_SUCCESS
        return null;
    }

    /**
     * @Description 接收支付宝支付通知，并转发到MQ（敏感数据，处理失败时可以重试）
     */
    @PostMapping(URL_API + URL_SHOP + "/alipay/notify")
    public Result<?> alipayNotify(@RequestParam Map<String, String> params) {
        // TODO 解析数据，校验签名合法且支付成功时，转发数据（多种支付方式统一数据结构）到MQ: QUEUE_TRADE_ORDER_SUCCESS
        return success();
    }
}
