package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.shop.support.enums.SpuType;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Sku extends Vo<Integer> {
    /**
     * 商品类型
     */
    private SpuType spuType;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 供应商自定义参数
     */
    private String supplierParam;
    /**
     * 前台划线价
     */
    private Integer marketPrice;
    /**
     * 销售单价
     */
    private Integer salePrice;
    /**
     * 已销售数量
     */
    private Integer soldNum;
}
