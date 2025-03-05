package org.isite.operation.task;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.SignScoreProperty;
import org.isite.operation.support.vo.SignScoreReward;
import org.isite.operation.support.vo.Task;
import org.springframework.stereotype.Component;
/**
 * @Description 每日签到送活动积分或VIP积分（不区分用户是否为会员），积分奖励系数（coefficient）配置用户连续签到的天数
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SignScoreExecutor extends ScoreTaskExecutor {
    /**
     * 根据用户连续签到获取任务奖励
     */
    @Override
    protected Reward getReward(Activity activity, Task task, EventDto eventDto) {
        int continuousCount = TypeUtils.cast(eventDto.getEventParam());
        SignScoreProperty signScoreProperty = TypeUtils.cast(task.getProperty());
        if (null == signScoreProperty || CollectionUtils.isEmpty(signScoreProperty.getRewards())) {
            return null;
        }
        SignScoreReward target = null;
        for (SignScoreReward reward : signScoreProperty.getRewards()) {
            Assert.notNull(reward.getContinuousCount(), "SignScoreReward#continuousCount cannot be null");
            if (continuousCount == reward.getContinuousCount()) {
                return reward;
            } else if (continuousCount > reward.getContinuousCount() &&
                    (null == target || target.getContinuousCount() < reward.getContinuousCount())) {
                target =  reward;
            }
        }
        return target;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {TaskType.OPERATION_SIGN_SCORE};
    }
}
