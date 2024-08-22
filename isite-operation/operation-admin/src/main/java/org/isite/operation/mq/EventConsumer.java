package org.isite.operation.mq;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.mq.Basic;
import org.isite.commons.web.mq.Consumer;
import org.isite.operation.activity.ActivityMonitor;
import org.isite.operation.activity.ActivityMonitorFactory;
import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.EventType;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.service.OngoingActivityService;
import org.isite.operation.task.TaskExecutor;
import org.isite.operation.task.TaskExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.function.Function;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.operation.data.enums.TaskType.values;

/**
 * @Description 运营任务事件消费者
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class EventConsumer implements Consumer<EventDto> {
    private TaskExecutorFactory taskExecutorFactory;
    private ActivityMonitorFactory activityMonitorFactory;
    private OngoingActivityService ongoingActivityService;

    @Override
    public Basic handle(@Validated EventDto eventDto) {
        EventType eventType = eventDto.getEventType();
        List<Activity> activityList = ongoingActivityService.findActivityList(eventType);
        if (isEmpty(activityList)) {
            return new Basic.Ack();
        }
        Function<Object, Object> converter = eventType.getConverter();
        if (null != converter && null != eventDto.getEventParam()) {
            eventDto.setEventParam(converter.apply(eventDto.getEventParam()));
        }
        activityList.forEach(activity -> handle(activity, eventDto));
        return new Basic.Ack();
    }

    /**
     * 处理行为消息
     */
    public void handle(Activity activity, EventDto eventDto) {
        ActivityMonitor monitor = activityMonitorFactory.get(activity.getTheme());
        if (null == monitor || monitor.participate(activity, eventDto.getUserId())) {
            for (TaskType taskType : values(eventDto.getEventType())) {
                TaskExecutor<?> taskExecutor = taskExecutorFactory.get(taskType);
                activity.getTasks().forEach(task -> {
                    if (taskType.equals(task.getTaskType())) {
                        try {
                            taskExecutor.execute(activity, task, eventDto);
                        } catch (Exception e) {
                            log.warn(toJsonString(eventDto), e);
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
