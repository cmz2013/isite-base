package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TradeOrderItem extends Vo<Long> {
    /**
     * 产品ID
     */
    private Integer spuId;
    /**
     * 产品名称
     */
    private String spuName;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 供应商自定义参数
     */
    private String supplierParam;
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
     * 支付积分
     */
    private Integer payScore;
    /**
     * 实际支付总金额(分)，不含服务费
     */
    private Integer payPrice;
    /**
     * 服务费(分)
     */
    private Integer serviceCharge;
}
