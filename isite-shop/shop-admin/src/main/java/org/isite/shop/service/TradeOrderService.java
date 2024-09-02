package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderMapper;
import org.isite.shop.po.TradeOrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TradeOrderService extends PoService<TradeOrderPo, Long> {

    @Autowired
    public TradeOrderService(TradeOrderMapper mapper) {
        super(mapper);
    }
}
