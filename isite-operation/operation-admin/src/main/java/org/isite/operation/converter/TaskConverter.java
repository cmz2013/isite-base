package org.isite.operation.converter;

import org.isite.operation.po.TaskPo;
import org.isite.operation.support.dto.TaskPostDto;
import org.isite.operation.support.vo.Task;
import org.isite.operation.support.vo.TaskPeriod;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.json.Jackson.parseObject;

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
        return isEmpty(taskPos) ? emptyList() :
                taskPos.stream().map(TaskConverter::toTask).collect(toList());
    }

    public static Task toTask(TaskPo taskPo) {
        Task task = new Task();
        task.setId(taskPo.getId());
        task.setTaskType(taskPo.getTaskType());
        task.setTitle(taskPo.getTitle());
        task.setRemark(taskPo.getRemark());
        if (isNotBlank(taskPo.getTaskPeriod())) {
            task.setTaskPeriod(parseObject(taskPo.getTaskPeriod(), TaskPeriod.class));
        }
        if (isNotBlank(taskPo.getProperty())) {
            task.setProperty(parseObject(taskPo.getProperty(), taskPo.getTaskType().getPropertyClass()));
        }
        return task;
    }

    public static TaskPo toTaskPo(TaskPostDto taskPostDto) {
        TaskPo taskPo = convert(taskPostDto, TaskPo::new);
        taskPo.setEventType(taskPo.getTaskType().getEventType());
        if (null == taskPo.getProperty()) {
            taskPo.setProperty(BLANK_STR);
        }
        if (null == taskPo.getRemark()) {
            taskPo.setRemark(BLANK_STR);
        }
        if (null == taskPo.getTaskPeriod()) {
            taskPo.setTaskPeriod(BLANK_STR);
        }
        return taskPo;
    }
}
