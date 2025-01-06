package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.shop.support.enums.TradeStatus;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrderGetDto {
    /**
     * 订单状态
     */
    private TradeStatus status;
}
