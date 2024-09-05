package org.isite.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.controller.BaseController;
import org.isite.shop.config.AlipayConfig;
import org.isite.shop.config.WxpayConfig;
import org.isite.shop.service.WxpayNoticeService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.alipay.api.internal.util.AlipaySignature.rsaCheckV1;
import static org.isite.commons.cloud.constants.UrlConstants.URL_API;
import static org.isite.commons.lang.Constants.FAILURE;
import static org.isite.commons.lang.Constants.SUCCESS;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.shop.converter.PayNoticeConverter.toAlipayNoticeDto;
import static org.isite.shop.converter.PayNoticeConverter.toWxpayNoticeDto;
import static org.isite.shop.converter.PayNoticeConverter.toWxpayNoticeMap;
import static org.isite.shop.support.constants.AlipayConstants.FIELD_TRADE_STATUS;
import static org.isite.shop.support.constants.AlipayConstants.TRADE_STATUS_FINISHED;
import static org.isite.shop.support.constants.AlipayConstants.TRADE_STATUS_SUCCESS;
import static org.isite.shop.support.constants.ShopConstants.QUEUE_TRADE_ORDER_SUCCESS;
import static org.isite.shop.support.constants.UrlConstants.URL_SHOP;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_TRADE_STATE;
import static org.isite.shop.support.constants.WxpayConstants.TRADE_STATE_SUCCESS;
import static org.isite.shop.support.constants.WxpayConstants.XML_FAIL;
import static org.isite.shop.support.constants.WxpayConstants.XML_SUCCESS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@RestController
public class PayNoticeController extends BaseController {

    private WxpayConfig wxpayConfig;
    private RabbitTemplate rabbitTemplate;
    private AlipayConfig alipayConfig;
    private WxpayNoticeService wxpayNoticeService;

    @Autowired
    public void setAlipayConfig(AlipayConfig alipayConfig) {
        this.alipayConfig = alipayConfig;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setWxpayConfig(WxpayConfig wxpayConfig) {
        this.wxpayConfig = wxpayConfig;
    }

    @Autowired
    public void setWxpayNoticeService(WxpayNoticeService wxpayNoticeService) {
        this.wxpayNoticeService = wxpayNoticeService;
    }

    /**
     * @Description 接收微信支付通知，验证签名并转发到MQ（异步处理尽快响应,敏感数据处理失败时可以重试）
     */
    @PostMapping(URL_API + URL_SHOP + "/wxpay/notify")
    public String wechatNotify(@RequestBody String xmlData) {
        try {
            Map<String, String> params = toWxpayNoticeMap(xmlData);
            // 验证签名
            boolean signVerified = wxpayNoticeService.isValidSignature(params, wxpayConfig.getMchKey());
            if (signVerified && TRADE_STATE_SUCCESS.equals(params.get(FIELD_TRADE_STATE))) {
                rabbitTemplate.convertAndSend(QUEUE_TRADE_ORDER_SUCCESS, toWxpayNoticeDto(params));
                return XML_SUCCESS;
            }
        } catch (Exception e) {
            log.error(xmlData, e);
        }
        return XML_FAIL;
    }

    /**
     * @Description 接收支付宝支付通知，验证签名并转发到MQ（异步处理尽快响应,敏感数据处理失败时可以重试）
     */
    @PostMapping(URL_API + URL_SHOP + "/alipay/notify")
    public String alipayNotify(@RequestParam Map<String, String> params) {
        try {
            boolean signVerified = rsaCheckV1(params, alipayConfig.getPublicKey(),
                    alipayConfig.getCharset(), alipayConfig.getSignType());
            String tradeStatus = params.get(FIELD_TRADE_STATUS);
            if (signVerified && (TRADE_STATUS_SUCCESS.equals(tradeStatus) || TRADE_STATUS_FINISHED.equals(tradeStatus))) {
                rabbitTemplate.convertAndSend(QUEUE_TRADE_ORDER_SUCCESS, toAlipayNoticeDto(params));
                return SUCCESS;
            } else {
                return FAILURE;
            }
        } catch (Exception e) {
            log.error(toJsonString(params), e);
        }
        return FAILURE;
    }
}
