package org.isite.operation.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.json.Jackson;
import org.isite.operation.po.TaskPo;
import org.isite.operation.support.dto.TaskPostDto;
import org.isite.operation.support.vo.Task;
import org.isite.operation.support.vo.TaskPeriod;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TaskConverter {

    private TaskConverter() {
    }

    /**
     * to Task
     */
    public static List<Task> toTasks(List<TaskPo> taskPos) {
        return CollectionUtils.isEmpty(taskPos) ? Collections.emptyList() :
                taskPos.stream().map(TaskConverter::toTask).collect(Collectors.toList());
    }

    public static Task toTask(TaskPo taskPo) {
        Task task = new Task();
        task.setId(taskPo.getId());
        task.setTaskType(taskPo.getTaskType());
        task.setTitle(taskPo.getTitle());
        task.setRemark(taskPo.getRemark());
        if (StringUtils.isNotBlank(taskPo.getTaskPeriod())) {
            task.setTaskPeriod(Jackson.parseObject(taskPo.getTaskPeriod(), TaskPeriod.class));
        }
        if (StringUtils.isNotBlank(taskPo.getProperty())) {
            task.setProperty(Jackson.parseObject(taskPo.getProperty(), taskPo.getTaskType().getPropertyClass()));
        }
        return task;
    }

    public static TaskPo toTaskPo(TaskPostDto taskPostDto) {
        TaskPo taskPo = DataConverter.convert(taskPostDto, TaskPo::new);
        taskPo.setEventType(taskPo.getTaskType().getEventType());
        if (null == taskPo.getProperty()) {
            taskPo.setProperty(Constants.BLANK_STR);
        }
        if (null == taskPo.getRemark()) {
            taskPo.setRemark(Constants.BLANK_STR);
        }
        if (null == taskPo.getTaskPeriod()) {
            taskPo.setTaskPeriod(Constants.BLANK_STR);
        }
        return taskPo;
    }
}
