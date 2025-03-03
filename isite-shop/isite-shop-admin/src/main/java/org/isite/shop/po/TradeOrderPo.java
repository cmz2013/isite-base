package org.isite.shop.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.fms.data.enums.PaymentType;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.security.data.enums.ClientIdentifier;
import org.isite.shop.support.enums.TradeStatus;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "trade_order")
public class TradeOrderPo extends Po<Long> {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 下单时间
     */
    private LocalDateTime orderTime;
    /**
     * 订单号
     */
    private Long orderNo;
    /**
     * 订单支付金额(分)，不含服务费
     */
    private Integer payPrice;
    /**
     * 订单来源渠道
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ClientIdentifier clientIdentifier;
    /**
     * 支付方式
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
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
     * 订单状态
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private TradeStatus status;
    /**
     * 服务费(分)
     */
    private Integer serviceCharge;
}
