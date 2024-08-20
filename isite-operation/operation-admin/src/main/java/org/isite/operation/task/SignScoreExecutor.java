package org.isite.operation.task;

import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.Task;
import org.isite.operation.service.SignLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.SIGN_SCORE;

/**
 * @Description 每日签到送积分，积分奖励系数（coefficient）配置用户连续签到的天数
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SignScoreExecutor extends ScoreTaskExecutor {

    private SignLogService signLogService;

    @Override
    protected Reward getReward(Task task, Object eventParam) {
        return signLogService.getReward(task, cast(eventParam));
    }

    @Autowired
    public void setSignLogService(SignLogService signLogService) {
        this.signLogService = signLogService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {SIGN_SCORE};
    }
}
