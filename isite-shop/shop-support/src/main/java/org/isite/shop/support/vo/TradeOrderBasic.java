package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;
import org.isite.shop.support.enums.TradeStatus;

import java.util.Date;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrderBasic extends Vo<Long> {
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
     * 支付时间
     */
    private Date payTime;
    /**
     * 订单状态
     */
    private TradeStatus tradeStatus;
}
