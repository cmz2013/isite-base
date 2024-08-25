package org.isite.operation.task;

import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.ScoreReward;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.USER_SCORE;

/**
 * @Description 积分任务父接口。使用活动积分可以兑换奖品
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ScoreTaskExecutor extends TaskExecutor<ScoreRecordPo> {
    private ScoreRecordService scoreRecordService;

    @Override
    protected long countTaskRecord(int activityId, int taskId, @Nullable Date startTime, long userId) {
        return scoreRecordService.countScoreRecord(activityId, taskId, startTime, userId);
    }

    @Override
    public void saveTaskRecord(Activity activity, ScoreRecordPo taskRecord, Reward reward) {
        if (null == reward) {
            return;
        }
        ScoreReward scoreReward = cast(reward);
        isTrue(scoreReward.getScoreValue() > ZERO, "scoreReward.scoreValue must be greater than 0");
        taskRecord.setScoreValue(scoreReward.getScoreValue());
        taskRecord.setScoreType(scoreReward.getScoreType());
        scoreRecordService.insert(taskRecord);
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {USER_SCORE};
    }
}
