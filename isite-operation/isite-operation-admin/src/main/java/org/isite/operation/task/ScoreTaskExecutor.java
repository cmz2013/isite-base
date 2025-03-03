package org.isite.operation.task;

import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.ScoreReward;
import org.isite.operation.support.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.support.enums.ScoreType.VIP_SCORE;
import static org.isite.operation.support.enums.TaskType.USER_SCORE;
import static org.isite.user.client.UserAccessor.getUserDetails;

/**
 * @Description 积分任务父接口。VIP用户完成任务获得VIP积分或活动积分，普通用户完成任务只能获得活动积分。使用活动积分可以兑换奖品
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ScoreTaskExecutor extends TaskExecutor<ScoreRecordPo> {
    private ScoreRecordService scoreRecordService;

    @Override
    protected long countTaskRecord(int activityId, int taskId, @Nullable LocalDateTime startTime, long userId) {
        return scoreRecordService.countScoreRecord(activityId, taskId, startTime, userId);
    }

    /**
     * VIP用户完成任务获得VIP积分
     */
    @Override
    protected Reward getReward(Activity activity, Task task, EventDto eventDto) {
        ScoreReward scoreReward = cast(super.getReward(activity, task, eventDto));
        if (null != scoreReward && VIP_SCORE == scoreReward.getScoreType()) {
            return getUserDetails(eventDto.getUserId()).isVip() ? scoreReward : null;
        }
        return scoreReward;
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
