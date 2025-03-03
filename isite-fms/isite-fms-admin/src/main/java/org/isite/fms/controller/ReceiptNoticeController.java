package org.isite.fms.controller;

import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.controller.BaseController;
import org.isite.fms.config.AlipayConfig;
import org.isite.fms.config.WxpayConfig;
import org.isite.fms.converter.ReceiptNoticeConverter;
import org.isite.fms.data.constants.AlipayConstants;
import org.isite.fms.data.constants.FmsConstants;
import org.isite.fms.data.constants.FmsUrls;
import org.isite.fms.data.constants.WxpayConstants;
import org.isite.fms.service.WxpayNoticeService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
 * @Description 收款通知接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@RestController
public class ReceiptNoticeController extends BaseController {

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
    @PostMapping(UrlConstants.URL_API + FmsUrls.URL_FMS + "/wxpay/notify")
    public String wechatNotify(@RequestBody String xmlData) {
        try {
            Map<String, String> params = ReceiptNoticeConverter.toWxpayNoticeMap(xmlData);
            // 验证签名
            boolean signVerified = wxpayNoticeService.isValidSignature(params, wxpayConfig.getMchKey());
            if (signVerified && WxpayConstants.TRADE_STATE_SUCCESS.equals(params.get(WxpayConstants.FIELD_TRADE_STATE))) {
                rabbitTemplate.convertAndSend(FmsConstants.QUEUE_RECEIPT_SUCCESS, ReceiptNoticeConverter.toWxpayNoticeDto(params));
                return WxpayConstants.XML_SUCCESS;
            }
        } catch (Exception e) {
            log.error(xmlData, e);
        }
        return WxpayConstants.XML_FAIL;
    }

    /**
     * @Description 接收支付宝支付通知，验证签名并转发到MQ（异步处理尽快响应,敏感数据处理失败时可以重试）
     */
    @PostMapping(UrlConstants.URL_API + FmsUrls.URL_FMS + "/alipay/notify")
    public String alipayNotify(@RequestParam Map<String, String> params) {
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getPublicKey(),
                    alipayConfig.getCharset(), alipayConfig.getSignType());
            String tradeStatus = params.get(AlipayConstants.FIELD_TRADE_STATUS);
            if (signVerified && (AlipayConstants.TRADE_STATUS_SUCCESS.equals(tradeStatus) ||
                    AlipayConstants.TRADE_STATUS_FINISHED.equals(tradeStatus))) {
                rabbitTemplate.convertAndSend(FmsConstants.QUEUE_RECEIPT_SUCCESS, ReceiptNoticeConverter.toAlipayNoticeDto(params));
                return Constants.SUCCESS;
            } else {
                return Constants.FAILURE;
            }
        } catch (Exception e) {
            log.error(Jackson.toJsonString(params), e);
        }
        return Constants.FAILURE;
    }
}
