package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;

/**
 * @Description 商品信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Sku extends Vo<Integer> {
    /**
     * 产品信息
     */
    private Spu spu;
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
    /**
     * 备注
     */
    private String remark;
}
