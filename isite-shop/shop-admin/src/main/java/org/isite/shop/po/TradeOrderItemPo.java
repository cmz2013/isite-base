package org.isite.shop.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

@Getter
@Setter
@Table(name = "trade_order_item")
public class TradeOrderItemPo extends Po<Long> {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 产品ID
     */
    private Integer spuId;
    /**
     * 产品名称
     */
    private String spuName;
    /**
     * 商品id
     */
    private Integer skuId;
    /**
     * 商品数量
     */
    private Integer skuCount;
    /**
     * 前台划线价(分)
     */
    private Integer marketPrice;
    /**
     * 成本单价(分)
     */
    private Integer costPrice;
    /**
     * 销售单价(分)
     */
    private Integer salePrice;
    /**
     * 优惠金额(分)
     */
    private Integer discountPrice;
    /**
     * 实际支付金额(分)
     */
    private Integer payPrice;
    /**
     * 服务费(分)
     */
    private Integer serviceCharge;
    /**
     * 支付积分
     */
    private Integer payScore;
    /**
     * 供应商自定义参数(Sku的supplierParam)
     */
    private String supplierParam;
}