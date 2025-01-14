package org.isite.shop.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Description 优惠券，一个订单条目只能使用一个优惠券，且优惠券金额必须小于等于订单条目金额才能使用。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "coupon")
public class CouponPo extends Po<Integer> {
    /**
     * 券名称
     */
    private String couponName;
    /**
     * 优惠金额(分)
     */
    private Integer discountPrice;
    /**
     * 有效期天数
     */
    private Integer expireDays;
    /**
     * 商品限定，空即不限定
     */
    private String skuIds;
    /**
     * 优惠券总数量
     */
    private Integer totalNum;
    /**
     * 已领取数量
     */
    private Integer receivedNum;
}
