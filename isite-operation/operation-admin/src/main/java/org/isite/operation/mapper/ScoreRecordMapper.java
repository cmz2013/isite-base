package org.isite.operation.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.po.ScoreRecordPo;
import org.springframework.stereotype.Repository;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ScoreRecordMapper extends PoMapper<ScoreRecordPo, Long> {
    /**
     * 统计用户总积分
     */
    long selectTotalScore(@Param("activityId") int activityId, @Param("userId") long userId);
}
