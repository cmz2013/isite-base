package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.shop.support.enums.PaymentType;
import org.isite.shop.support.enums.SourceType;
import org.isite.shop.support.enums.TradeStatus;

import java.util.Date;
import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrder extends Vo<Long> {
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
     * 订单来源渠道
     */
    private SourceType sourceType;
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
     * 订单条目
     */
    private List<TradeOrderItem> items;
}
