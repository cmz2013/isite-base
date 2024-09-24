package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;
import org.isite.security.data.enums.ClientIdentifier;
import org.isite.shop.support.enums.PaymentType;
import org.isite.shop.support.enums.TradeStatus;

import java.util.Date;
import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrderDetails extends Vo<Long> {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 订单号
     */
    private Long orderNumber;
    /**
     * 支付金额(分)
     */
    private Integer payPrice;
    /**
     * 订单来源渠道
     */
    private ClientIdentifier clientIdentifier;
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
     * 订单状态
     */
    private TradeStatus tradeStatus;
    /**
     * 服务费(分)
     */
    private Integer serviceCharge;
    /**
     * 订单条目明细
     */
    private List<TradeOrderItem> orderItems;
}
