package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Valid
public class TradeOrderItemPostDto {
    /**
     * 商品id
     */
    @NotNull
    private Integer skuId;
    /**
     * 商品数量
     */
    @NotNull
    private Integer skuCount;
    /**
     * 用户优惠券领取记录id
     */
    private Integer couponRecordId;
    /**
     * 支付积分(一个积分可以抵1分钱)
     */
    private Integer payScore;
}
