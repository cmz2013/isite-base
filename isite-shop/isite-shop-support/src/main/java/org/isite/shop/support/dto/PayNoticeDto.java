package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.shop.support.enums.PaymentType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private Long orderNo;
    /**
     * 支付方式
     */
    private PaymentType paymentType;
    /**
     * 支付单唯一编码
     */
    private Long paymentNo;
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    /**
     * 支付金额(分)，不含服务费
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
