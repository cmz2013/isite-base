package org.isite.operation.task;

import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.AnswerInviteParam;
import org.isite.operation.support.vo.AnswerPrizeProperty;
import org.isite.operation.support.vo.TaskProperty;
import org.springframework.stereotype.Component;
/**
 * @Description 答疑奖品任务接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class AnswerPrizeExecutor extends PrizeTaskExecutor {
    /**
     * 校验是否首答和响应时间
     */
    @Override
    protected boolean checkTaskProperty(TaskProperty<?> taskProperty, EventDto eventDto) {
        AnswerPrizeProperty answerPrizeProperty = TypeUtils.cast(taskProperty);
        AnswerInviteParam eventParam = TypeUtils.cast(eventDto.getEventParam());
        if (null == answerPrizeProperty.getFirstReply() ||
                answerPrizeProperty.getFirstReply().equals(eventParam.getFirstAnswer())) {
           return (null == answerPrizeProperty.getMaxResponseTime() ||
                   answerPrizeProperty.getMaxResponseTime() >= eventParam.getResponseTime());
        }
        return Boolean.FALSE;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {TaskType.QUESTION_REPLY_PRIZE};
    }
}
