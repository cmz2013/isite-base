package org.isite.operation.task;

import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.ScoreReward;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.ACCEPT_REPLY_SCORE;
import static org.isite.operation.data.enums.TaskType.USER_SCORE;

/**
 * @Description 积分任务父接口。使用活动积分可以兑换奖品
 * VipScoreGiver发放会员积分，可以设置库存，限制发放的总份数；
 * ScoreTaskExecutor发放会员积分时，可以通过任务次数限制每人的发放次数，但是不能限制总份数
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ScoreTaskExecutor extends TaskExecutor<ScoreRecordPo> {
    private ScoreRecordService scoreRecordService;

    @Override
    protected long countTaskRecord(int activityId, int taskId, Date startTime, long userId) {
        return scoreRecordService.countScoreRecord(activityId, taskId, startTime, userId);
    }

    @Override
    public void saveTaskRecord(Activity activity, ScoreRecordPo taskRecord, Reward reward) {
        ScoreReward scoreReward = cast(reward);
        if (null == scoreReward) {
            return;
        }
        notNull(scoreReward.getScoreValue(), "scoreReward.scoreValue must not be null");
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
        return new TaskType[] {USER_SCORE, ACCEPT_REPLY_SCORE};
    }
}
