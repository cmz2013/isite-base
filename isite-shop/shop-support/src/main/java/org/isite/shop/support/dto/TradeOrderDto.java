package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.shop.support.enums.SourceType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.THOUSAND;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrderDto {
    /**
     * 统一订单号
     */
    private Long orderNumber;
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;
    /**
     * 来源渠道
     */
    private SourceType sourceType;
    /**
     * 订单条目.
     */
    @Valid
    @Size(min = ONE, max = THOUSAND)
    private List<TradeOrderItemDto> orderItems;
}
