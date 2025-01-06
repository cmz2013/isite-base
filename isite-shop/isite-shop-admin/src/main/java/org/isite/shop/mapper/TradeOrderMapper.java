package org.isite.shop.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.shop.po.TradeOrderPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TradeOrderMapper extends PoMapper<TradeOrderPo, Long> {
}
