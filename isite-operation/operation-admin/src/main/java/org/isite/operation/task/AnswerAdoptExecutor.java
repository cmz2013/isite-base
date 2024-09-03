package org.isite.operation.task;

import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Task;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.support.enums.TaskType.QUESTION_REPLY_ADOPT_PRIZE;

/**
 * @Description 提问人采纳答案时，给回复人送奖品。eventDto#userId为提问人ID，eventParam为回答人ID
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class AnswerAdoptExecutor extends PrizeTaskExecutor {

    @Override
    protected PrizeRecordPo createTaskRecord(
            EventDto eventDto, Activity activity, Task task, Date periodStartTime, long taskNumber) {
        PrizeRecordPo prizeRecordPo = super.createTaskRecord(eventDto, activity, task, periodStartTime, taskNumber);
        prizeRecordPo.setUserId(cast(eventDto.getEventParam()));
        return prizeRecordPo;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {QUESTION_REPLY_ADOPT_PRIZE};
    }
}
