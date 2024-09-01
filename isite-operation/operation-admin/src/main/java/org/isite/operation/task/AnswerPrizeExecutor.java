package org.isite.operation.task;

import org.isite.operation.support.dto.OperationEventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.AnswerEventParam;
import org.isite.operation.support.vo.AnswerPrizeProperty;
import org.isite.operation.support.vo.TaskProperty;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.support.enums.TaskType.QUESTION_ANSWER_PRIZE;

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
    protected boolean checkTaskProperty(TaskProperty<?> taskProperty, OperationEventDto operationEventDto) {
        AnswerPrizeProperty answerPrizeProperty = cast(taskProperty);
        AnswerEventParam eventParam = cast(operationEventDto.getEventParam());
        if (null == answerPrizeProperty.getFirstReply() ||
                answerPrizeProperty.getFirstReply().equals(eventParam.getFirstAnswer())) {
           return (null == answerPrizeProperty.getMaxResponseTime() ||
                   answerPrizeProperty.getMaxResponseTime() >= eventParam.getResponseTime());
        }
        return FALSE;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {QUESTION_ANSWER_PRIZE};
    }
}
