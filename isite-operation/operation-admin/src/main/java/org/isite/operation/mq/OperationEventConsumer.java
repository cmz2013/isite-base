package org.isite.operation.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.operation.activity.ActivityMonitor;
import org.isite.operation.activity.ActivityMonitorFactory;
import org.isite.operation.support.dto.OperationEventDto;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.task.TaskExecutor;
import org.isite.operation.task.TaskExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.operation.support.enums.TaskType.values;

/**
 * @Description 运营任务事件消费者
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class OperationEventConsumer implements Consumer<OperationEventDto> {
    private TaskExecutorFactory taskExecutorFactory;
    private ActivityMonitorFactory activityMonitorFactory;
    private OngoingActivityService ongoingActivityService;

    @Override
    @Validated
    public Basic handle(OperationEventDto operationEventDto) {
        EventType eventType = operationEventDto.getEventType();
        List<Activity> activityList = ongoingActivityService.findOngoingActivities(eventType);
        if (isEmpty(activityList)) {
            return new Basic.Ack();
        }
        if (null != eventType.getConverter() && null != operationEventDto.getEventParam()) {
            operationEventDto.setEventParam(eventType.getConverter().apply(operationEventDto.getEventParam()));
        }
        activityList.forEach(activity -> handle(activity, operationEventDto));
        return new Basic.Ack();
    }

    /**
     * 处理行为消息
     */
    public void handle(Activity activity, OperationEventDto operationEventDto) {
        ActivityMonitor monitor = activityMonitorFactory.get(activity.getTheme());
        if (null == monitor || monitor.participate(activity, operationEventDto.getUserId())) {
            for (TaskType taskType : values(operationEventDto.getEventType())) {
                TaskExecutor<?> taskExecutor = taskExecutorFactory.get(taskType);
                activity.getTasks().forEach(task -> {
                    if (taskType.equals(task.getTaskType())) {
                        try {
                            taskExecutor.execute(activity, task, operationEventDto);
                        } catch (Exception e) {
                            log.warn(toJsonString(operationEventDto), e);
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
