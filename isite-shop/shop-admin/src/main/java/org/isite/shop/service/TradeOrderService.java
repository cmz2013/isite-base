package org.isite.shop.service;

import org.isite.mybatis.service.PoService;
import org.isite.shop.mapper.TradeOrderMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.isite.shop.po.TradeOrderPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TradeOrderService extends PoService<TradeOrderPo, Long> {

    @Autowired
    public TradeOrderService(TradeOrderMapper mapper) {
        super(mapper);
    }

    /**
     *  计算订单总金额
     */
    private int sumPayPrice(List<TradeOrderItemPo> itemPos) {
        int totalPrice = ZERO;
        for (TradeOrderItemPo itemPo : itemPos) {
            totalPrice += itemPo.getPayPrice();
        }
        return totalPrice;
    }
}
