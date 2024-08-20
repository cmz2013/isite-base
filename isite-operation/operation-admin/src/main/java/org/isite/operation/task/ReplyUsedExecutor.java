package org.isite.operation.task;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Task;
import org.springframework.stereotype.Component;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.constants.OperationConstants.FIELD_EVENT_PARAM;
import static org.isite.operation.data.enums.TaskType.ADOPT_REPLY_PRIZE;
import static org.isite.user.data.constant.UserConstants.FIELD_USER_ID;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @Description 答疑奖品任务接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ReplyUsedExecutor extends PrizeTaskExecutor {
    /**
     * @Description 提问人采纳答案时，给回复人送奖品
     * 注意：eventDto#userId是提问人ID，不能直接修改userId为回复人ID，否则会影响提问人任务执行
     */
    @Override
    public void execute(Activity activity, Task task, EventDto eventDto) {
        EventDto target = new EventDto();
        copyProperties(eventDto, target, FIELD_USER_ID, FIELD_EVENT_PARAM);
        target.setUserId(cast(eventDto.getEventParam()));
        super.execute(activity, task, eventDto);
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {ADOPT_REPLY_PRIZE};
    }
}
