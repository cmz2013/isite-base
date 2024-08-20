package org.isite.operation.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.po.PrizeRecordPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface PrizeRecordMapper extends PoMapper<PrizeRecordPo, Long> {
    /**
     * 查询用户未领取的领奖记录
     * @param prizeId 可以为空
     */
    PrizeRecordPo selectOneNotReceive(
            @Param("activityId") int activityId, @Param("prizeId") Integer prizeId,
            @Param("userId") long userId);
}
