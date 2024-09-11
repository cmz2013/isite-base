package org.isite.operation.mapper;

import org.apache.ibatis.annotations.Param;
import org.isite.mybatis.mapper.PoMapper;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.support.enums.ScoreType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Repository
public interface ScoreRecordMapper extends PoMapper<ScoreRecordPo, Long> {
    /**
     * 统计用户可用的总积分
     */
    int sumAvailableScore(
            @Nullable @Param("activityId") Integer activityId,
            @Param("userId") long userId,
            @Param("scoreType") ScoreType scoreType,
            @Nullable @Param("startTime") Date startTime,
            @Nullable @Param("endTime") Date endTime);

    /**
     * 按顺序查询一条可用的积分
     */
    ScoreRecordPo selectOneAvailableScore(
            @Nullable @Param("activityId") Integer activityId,
            @Param("userId") long userId,
            @Param("scoreType") ScoreType scoreType,
            @Nullable @Param("startTime") Date startTime);

    /**
     * 查询用户VIP积分记录，VIP积分有效期为1年
     */
    List<ScoreRecordPo> selectVipScoreRecord(
            @Param("userId") long userId,
            @Param("startTime") Date startTime);
}
