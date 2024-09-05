package org.isite.shop.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;

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
     * 前台划线价（分）
     */
    private Integer marketPrice;
    /**
     * 销售单价（分）
     */
    private Integer salePrice;
    /**
     * 会员单价(分)，VIP会员可以享受折扣优惠
     */
    private Integer vipPrice;
    /**
     * 已销售数量
     */
    private Integer soldNum;
    /**
     * 备注
     */
    private String remark;
}
