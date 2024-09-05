package org.isite.shop.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.shop.po.CouponPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface CouponMapper extends PoMapper<CouponPo, Integer> {
}
