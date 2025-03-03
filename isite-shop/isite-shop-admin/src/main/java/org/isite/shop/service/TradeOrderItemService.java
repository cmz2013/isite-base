package org.isite.shop.service;

import org.isite.commons.lang.Constants;
import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderItemMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TradeOrderItemService extends PoService<TradeOrderItemPo, Long> {

    @Autowired
    public TradeOrderItemService(TradeOrderItemMapper mapper) {
        super(mapper);
    }

    /**
     * 用户最近一年内累计消费金额
     */
    public int sumLastYearPayPrice(long userId) {
        return ((TradeOrderItemMapper) getMapper()).sumPayPrice(userId, LocalDateTime.now().minusYears(Constants.ONE));
    }
}
