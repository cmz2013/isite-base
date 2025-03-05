package org.isite.operation.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.operation.activity.ActivityMonitor;
import org.isite.operation.activity.ActivityMonitorFactory;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.task.TaskExecutor;
import org.isite.operation.task.TaskExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
/**
 * @Description 运营任务事件消费者
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class OperationEventConsumer implements Consumer<EventDto> {
    private TaskExecutorFactory taskExecutorFactory;
    private ActivityMonitorFactory activityMonitorFactory;
    private OngoingActivityService ongoingActivityService;

    @Override
    @Validated
    public Basic handle(EventDto eventDto) {
        EventType eventType = eventDto.getEventType();
        List<Activity> activityList = ongoingActivityService.findOngoingActivities(eventType);
        if (CollectionUtils.isEmpty(activityList)) {
            return new Basic.Ack();
        }
        if (null != eventType.getConverter() && null != eventDto.getEventParam()) {
            eventDto.setEventParam(eventType.getConverter().apply(eventDto.getEventParam()));
        }
        activityList.forEach(activity -> handle(activity, eventDto));
        return new Basic.Ack();
    }

    /**
     * 处理行为消息
     */
    public void handle(Activity activity, EventDto eventDto) {
        ActivityMonitor monitor = activityMonitorFactory.get(activity.getTheme());
        if (null == monitor || monitor.doTask(activity, eventDto.getUserId())) {
            for (TaskType taskType : TaskType.values(eventDto.getEventType())) {
                TaskExecutor<?> taskExecutor = taskExecutorFactory.get(taskType);
                activity.getTasks().forEach(task -> {
                    if (taskType.equals(task.getTaskType())) {
                        try {
                            taskExecutor.execute(activity, task, eventDto);
                        } catch (Exception e) {
                            log.warn(Jackson.toJsonString(eventDto), e);
                        }
                    }
                });
            }
        }
    }

    @Autowired
    public void setOngoingActivityService(OngoingActivityService ongoingActivityService) {
        this.ongoingActivityService = ongoingActivityService;
    }

    @Autowired
    public void setActivityMonitorFactory(ActivityMonitorFactory activityMonitorFactory) {
        this.activityMonitorFactory = activityMonitorFactory;
    }

    @Autowired
    public void setTaskExecutorFactory(TaskExecutorFactory taskExecutorFactory) {
        this.taskExecutorFactory = taskExecutorFactory;
    }
}
