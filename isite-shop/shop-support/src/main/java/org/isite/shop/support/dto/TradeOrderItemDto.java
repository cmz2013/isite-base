package org.isite.shop.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.shop.support.enums.SpuType;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TradeOrderItemDto {
    /**
     * 商品类型
     */
    @NotNull
    private SpuType spuType;
    /**
     * 商品名称
     */
    @NotNull
    private String skuName;
    /**
     * 商品数量
     */
    @NotNull
    private Integer skuCount;
    /**
     * 供应商自定义参数
     */
    private String supplierParam;
}