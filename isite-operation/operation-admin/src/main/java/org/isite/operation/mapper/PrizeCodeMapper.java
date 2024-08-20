package org.isite.operation.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.po.PrizeCodePo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface PrizeCodeMapper extends PoMapper<PrizeCodePo, Integer> {
    /**
     * 根据奖品ID查询未使用的兑奖码
     * @param prizeId 奖品ID
     * @return 未使用的兑奖码
     */
    PrizeCodePo selectOneUnused(@Param("prizeId") Integer prizeId);
}
