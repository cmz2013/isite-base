package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.SpuMapper;
import org.isite.shop.po.SpuPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class SpuService extends PoService<SpuPo, Integer> {

    @Autowired
    public SpuService(SpuMapper mapper) {
        super(mapper);
    }
}
