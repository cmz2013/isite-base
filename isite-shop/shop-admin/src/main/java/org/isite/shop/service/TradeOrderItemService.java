package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderItemMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.utils.DateUtils.getTimeBeforeYear;

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
        return ((TradeOrderItemMapper) getMapper()).sumPayPrice(userId, getTimeBeforeYear(ONE));
    }
}
