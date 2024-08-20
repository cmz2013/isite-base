package org.isite.operation.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeDeliverDto {
    /**
     * 订单号
     */
    @NotBlank
    private String orderNum;
}
