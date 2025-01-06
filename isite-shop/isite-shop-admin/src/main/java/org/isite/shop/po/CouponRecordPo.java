package org.isite.shop.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 优惠券，一个订单条目只能使用一个优惠券，且优惠券金额必须小于等于订单条目金额才能使用。
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "coupon_record")
public class CouponRecordPo extends Po<Integer> {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 优惠券ID
     */
    private Integer couponId;
    /**
     * 券名称
     */
    private String couponName;
    /**
     * 优惠金额(分)
     */
    private Integer discountPrice;
    /**
     * 商品限定，空即不限定
     */
    private String skuIds;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 优惠券使用状态
     */
    private Boolean used;
}
