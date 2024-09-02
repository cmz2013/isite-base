package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderItemMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TradeOrderItemService extends PoService<TradeOrderItemPo, Long> {

    @Autowired
    public TradeOrderItemService(TradeOrderItemMapper mapper) {
        super(mapper);
    }
}
