package org.isite.operation.task;

import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.ScoreType;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.ScoreReward;
import org.isite.operation.support.vo.Task;
import org.isite.user.client.UserAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        ScoreReward scoreReward = TypeUtils.cast(super.getReward(activity, task, eventDto));
        if (null != scoreReward && ScoreType.VIP_SCORE == scoreReward.getScoreType()) {
            return UserAccessor.getUserDetails(eventDto.getUserId()).isVip() ? scoreReward : null;
        }
        return scoreReward;
    }

    @Override
    public void saveTaskRecord(Activity activity, ScoreRecordPo taskRecord, Reward reward) {
        if (null == reward) {
            return;
        }
        ScoreReward scoreReward = TypeUtils.cast(reward);
        Assert.isTrue(scoreReward.getScoreValue() > Constants.ZERO, "score must be greater than 0");
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
        return new TaskType[] {TaskType.USER_SCORE};
    }
}
