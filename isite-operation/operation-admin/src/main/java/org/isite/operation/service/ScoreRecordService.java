package org.isite.operation.service;

import org.isite.operation.mapper.ScoreRecordMapper;
import org.isite.operation.po.ScoreRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Date;

import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ScoreRecordService extends TaskRecordService<ScoreRecordPo> {

    @Autowired
    public ScoreRecordService(ScoreRecordMapper scoreRecordMapper) {
        super(scoreRecordMapper);
    }

    /**
     * 统计积分记录
     */
    public int countScoreRecord(int activityId, int taskId, @Nullable Date startTime, long userId) {
        Weekend<ScoreRecordPo> weekend = of(ScoreRecordPo.class);
        WeekendCriteria<ScoreRecordPo, Object> criteria = weekend.weekendCriteria()
                .andEqualTo(ScoreRecordPo::getUserId, userId)
                .andEqualTo(ScoreRecordPo::getActivityId, activityId)
                .andEqualTo(ScoreRecordPo::getTaskId, taskId);

        if (null != startTime) {
            criteria.andGreaterThanOrEqualTo(ScoreRecordPo::getFinishTime, startTime);
        }
        return this.count(weekend);
    }

    /**
     * 统计用户总积分
     */
    public long findTotalScore(int activityId, long userId) {
        return ((ScoreRecordMapper) getMapper()).selectTotalScore(activityId, userId);
    }
}
