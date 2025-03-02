package org.isite.shop.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.CouponRecordMapper;
import org.isite.shop.po.CouponRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class CouponRecordService extends PoService<CouponRecordPo, Integer> {

    @Autowired
    public CouponRecordService(CouponRecordMapper mapper) {
        super(mapper);
    }

    /**
     * 更新优惠券使用状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCouponUsed(List<Integer> couponRecordIds) {
        if (CollectionUtils.isNotEmpty(couponRecordIds)) {
            CouponRecordPo couponRecordPo = new CouponRecordPo();
            couponRecordPo.setUsed(Boolean.TRUE);
            couponRecordIds.forEach(id -> {
                couponRecordPo.setId(id);
                this.updateSelectiveById(couponRecordPo);
            });
        }
    }
}
