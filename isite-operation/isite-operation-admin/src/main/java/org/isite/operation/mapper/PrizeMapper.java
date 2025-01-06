package org.isite.operation.mapper;

import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.po.PrizePo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface PrizeMapper extends PoMapper<PrizePo, Integer> {
}