package org.isite.operation.task;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.ReplyEventParam;
import org.isite.operation.data.vo.ReplyPrizeProperty;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.TaskProperty;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.data.enums.TaskType.REPLY_PRIZE;

/**
 * @Description 答疑奖品任务接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ReplyPrizeExecutor extends PrizeTaskExecutor {
    /**
     * 校验是否首答和响应时间
     */
    @Override
    protected boolean checkTaskProperty(TaskProperty<? extends Reward> taskProperty, EventDto eventDto) {
        ReplyPrizeProperty replyPrizeProperty = cast(taskProperty);
        ReplyEventParam eventParam = parseObject(eventDto.getEventParam().toString(), ReplyEventParam.class);
        if (null == replyPrizeProperty.getFirstReply() ||
                replyPrizeProperty.getFirstReply().equals(eventParam.getFirstAnswer())) {
           return (null == replyPrizeProperty.getMaxResponseTime() ||
                   replyPrizeProperty.getMaxResponseTime() >= eventParam.getResponseTime());
        }
        return FALSE;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {REPLY_PRIZE};
    }
}
