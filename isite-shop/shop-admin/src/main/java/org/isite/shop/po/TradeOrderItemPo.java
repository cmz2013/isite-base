package org.isite.shop.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;
import org.isite.shop.support.enums.SpuType;

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
     * 商品id
     */
    private Integer skuId;
    /**
     * 商品类型
     */
    private SpuType spuType;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品数量
     */
    private Integer skuCount;
    /**
     * 前台划线价
     */
    private Integer marketPrice;
    /**
     * 成本单价
     */
    private Integer costPrice;
    /**
     * 销售单价
     */
    private Integer salePrice;
    /**
     * 优惠金额
     */
    private Integer discountPrice;
    /**
     * 实际支付金额
     */
    private Integer payPrice;
    /**
     * 服务费
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