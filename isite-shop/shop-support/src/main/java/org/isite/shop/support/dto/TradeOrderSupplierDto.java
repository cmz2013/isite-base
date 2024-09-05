package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.THOUSAND;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrderSupplierDto {
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;
    /**
     * 订单条目.
     */
    @Valid
    @Size(min = ONE, max = THOUSAND)
    private List<TradeOrderSkuDto> skus;
}
