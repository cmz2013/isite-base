package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
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
    @Size(min = Constants.ONE, max = Constants.THOUSAND)
    private List<TradeOrderSkuDto> skuDtos;
}
