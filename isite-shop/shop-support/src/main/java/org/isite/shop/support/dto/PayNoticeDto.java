package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.shop.support.enums.PaymentType;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 订单支付成功通知参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PayNoticeDto {
    /**
     * 订单号
     */
    @NotNull
    private Long orderNumber;
    /**
     * 支付方式
     */
    private PaymentType paymentType;
    /**
     * 支付单唯一编码
     */
    private Long paymentNumber;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 支付金额(分)
     */
    private Integer payPrice;
    /**
     * 服务费(分)
     */
    private Integer serviceCharge;
    /**
     * 商家数据包，原样返回。
     */
    private String attach;
}
