package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.CouponMapper;
import org.isite.shop.po.CouponPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class CouponService extends PoService<CouponPo, Integer> {

    @Autowired
    public CouponService(CouponMapper mapper) {
        super(mapper);
    }
}
