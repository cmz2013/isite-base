package org.isite.shop.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.shop.support.enums.SourceType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "trade_order")
public class TradeOrderPo extends Po<Long> {
    /**
     * 统一订单号
     */
    private Long orderNumber;
    /**
     * 支付单唯一编码
     */
    private Long paymentNumber;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 来源渠道
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private SourceType sourceType;

}
