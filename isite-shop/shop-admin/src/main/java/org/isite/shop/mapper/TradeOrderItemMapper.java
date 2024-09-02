package org.isite.shop.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TradeOrderItemMapper extends PoMapper<TradeOrderItemPo, Long> {
}
