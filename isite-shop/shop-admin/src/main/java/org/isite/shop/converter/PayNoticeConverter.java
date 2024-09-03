package org.isite.shop.converter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.isite.shop.support.constants.AlipayConstants;
import org.isite.shop.support.constants.WxpayConstants;
import org.isite.shop.support.dto.PayNoticeDto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.DateUtils.PATTERN_DATETIME;
import static org.isite.commons.lang.utils.DateUtils.PATTERN_DATETIME_DIVIDE;
import static org.isite.commons.lang.utils.DateUtils.parseDate;
import static org.isite.shop.support.constants.AlipayConstants.FIELD_GMT_PAYMENT;
import static org.isite.shop.support.constants.AlipayConstants.FIELD_PASSBACK_PARAMS;
import static org.isite.shop.support.constants.AlipayConstants.FIELD_TOTAL_AMOUNT;
import static org.isite.shop.support.constants.AlipayConstants.FIELD_TRADE_NO;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_ATTACH;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_TIME_END;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_TOTAL_FEE;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_TRANSACTION_ID;
import static org.isite.shop.support.enums.PaymentType.ALIPAY;
import static org.isite.shop.support.enums.PaymentType.WXPAY;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PayNoticeConverter {

    private PayNoticeConverter() {
    }

    /**
     * 支付宝支付成功通知参数转换为DTO对象
     */
    public static PayNoticeDto toAlipayNoticeDto(Map<String, String> params) throws ParseException {
        PayNoticeDto payNoticeDto = new PayNoticeDto();
        payNoticeDto.setServiceCharge(ZERO);
        payNoticeDto.setPaymentType(ALIPAY);
        payNoticeDto.setPaymentNumber(parseLong(params.get(AlipayConstants.FIELD_OUT_TRADE_NO)));
        payNoticeDto.setPayTime(parseDate(params.get(FIELD_GMT_PAYMENT), PATTERN_DATETIME_DIVIDE));
        payNoticeDto.setAttach(params.get(FIELD_PASSBACK_PARAMS));
        payNoticeDto.setPaymentNumber(parseLong(params.get(FIELD_TRADE_NO)));
        payNoticeDto.setPayPrice(new BigDecimal(params.get(FIELD_TOTAL_AMOUNT))
                .multiply(new BigDecimal("100")).intValue());
        return payNoticeDto;
    }

    /**
     * 微信支付成功通知参数转换为DTO对象
     */
    public static PayNoticeDto toWxpayNoticeDto(Map<String, String> params) throws ParseException {
        PayNoticeDto payNoticeDto = new PayNoticeDto();
        payNoticeDto.setServiceCharge(ZERO);
        payNoticeDto.setPaymentType(WXPAY);
        payNoticeDto.setPaymentNumber(parseLong(params.get(WxpayConstants.FIELD_OUT_TRADE_NO)));
        payNoticeDto.setPayTime(parseDate(params.get(FIELD_TIME_END), PATTERN_DATETIME));
        payNoticeDto.setAttach(params.get(FIELD_ATTACH));
        payNoticeDto.setPaymentNumber(parseLong(params.get(FIELD_TRANSACTION_ID)));
        payNoticeDto.setPayPrice(parseInt(params.get(FIELD_TOTAL_FEE)));
        return payNoticeDto;
    }

    /**
     * 解析XML数据
     */
    public static Map<String, String> toWxpayNoticeMap(String xmlData) throws DocumentException {
        Map<String, String> params = new HashMap<>();
        Document document = DocumentHelper.parseText(xmlData);
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Element element : elements) {
            params.put(element.getName(), element.getText());
        }
        return params;
    }
}
