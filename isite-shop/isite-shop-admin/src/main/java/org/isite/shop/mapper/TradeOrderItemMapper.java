package org.isite.shop.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.shop.po.TradeOrderItemPo;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface TradeOrderItemMapper extends PoMapper<TradeOrderItemPo, Long> {

    int sumPayPrice(@Param("userId") long userId, @Param("startTime") Date startTime);
}
