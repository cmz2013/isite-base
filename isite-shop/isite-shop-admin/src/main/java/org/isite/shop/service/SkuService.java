package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.SkuMapper;
import org.isite.shop.po.SkuPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class SkuService extends PoService<SkuPo, Integer> {

    @Autowired
    public SkuService(SkuMapper mapper) {
        super(mapper);
    }

    public int updateSoldNum(Integer skuId, Integer skuNum) {
        return ((SkuMapper) getMapper()).updateSoldNum(skuId, skuNum);
    }
}
