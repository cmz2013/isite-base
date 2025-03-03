package org.isite.fms.converter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.DateUtils;
import org.isite.fms.data.constants.AlipayConstants;
import org.isite.fms.data.constants.WxpayConstants;
import org.isite.fms.data.dto.ReceiptNoticeDto;
import org.isite.fms.data.enums.PaymentType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ReceiptNoticeConverter {

    private ReceiptNoticeConverter() {
    }

    /**
     * 支付宝支付成功通知参数转换为DTO对象
     */
    public static ReceiptNoticeDto toAlipayNoticeDto(Map<String, String> params) {
        ReceiptNoticeDto receiptNoticeDto = new ReceiptNoticeDto();
        receiptNoticeDto.setServiceCharge(Constants.ZERO);
        receiptNoticeDto.setPaymentType(PaymentType.ALIPAY);
        receiptNoticeDto.setOrderNo(Long.parseLong(params.get(AlipayConstants.FIELD_OUT_TRADE_NO)));
        receiptNoticeDto.setPayTime(DateUtils.parseDateTime(
                params.get(AlipayConstants.FIELD_GMT_PAYMENT), DateUtils.PATTERN_DATETIME_DIVIDE));
        receiptNoticeDto.setAttach(params.get(AlipayConstants.FIELD_PASSBACK_PARAMS));
        receiptNoticeDto.setPaymentNo(Long.parseLong(params.get(AlipayConstants.FIELD_TRADE_NO)));
        receiptNoticeDto.setPayPrice(new BigDecimal(params.get(AlipayConstants.FIELD_TOTAL_AMOUNT))
                .multiply(new BigDecimal("100")).intValue());
        return receiptNoticeDto;
    }

    /**
     * 微信支付成功通知参数转换为DTO对象
     */
    public static ReceiptNoticeDto toWxpayNoticeDto(Map<String, String> params) {
        ReceiptNoticeDto receiptNoticeDto = new ReceiptNoticeDto();
        receiptNoticeDto.setServiceCharge(Constants.ZERO);
        receiptNoticeDto.setPaymentType(PaymentType.WXPAY);
        receiptNoticeDto.setOrderNo(Long.parseLong(params.get(WxpayConstants.FIELD_OUT_TRADE_NO)));
        receiptNoticeDto.setPayTime(DateUtils.parseDateTime(params.get(WxpayConstants.FIELD_TIME_END), DateUtils.PATTERN_DATETIME));
        receiptNoticeDto.setAttach(params.get(WxpayConstants.FIELD_ATTACH));
        receiptNoticeDto.setPaymentNo(Long.parseLong(params.get(WxpayConstants.FIELD_TRANSACTION_ID)));
        receiptNoticeDto.setPayPrice(Integer.parseInt(params.get(WxpayConstants.FIELD_TOTAL_FEE)));
        return receiptNoticeDto;
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
