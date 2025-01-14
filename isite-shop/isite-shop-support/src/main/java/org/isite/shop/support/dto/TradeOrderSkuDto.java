package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TradeOrderSkuDto {
    /**
     * 产品名称
     */
    @NotNull
    private String spuName;
    /**
     * 供应商自定义参数
     */
    private String supplierParam;
    /**
     * 商品数量
     */
    @NotNull
    private Integer skuNum;
}