package org.isite.operation.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.lang.Constants;
import org.isite.mybatis.service.PoService;
import org.isite.operation.activity.ActivityAssert;
import org.isite.operation.mapper.TaskMapper;
import org.isite.operation.po.TaskPo;
import org.isite.operation.po.TaskRecordPo;
import org.isite.operation.support.vo.Task;
import org.isite.operation.task.TaskExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class TaskService extends PoService<TaskPo, Integer> {
    private TaskExecutorFactory taskExecutorFactory;

    @Autowired
    public TaskService(TaskMapper taskMapper) {
        super(taskMapper);
    }

    /**
     * 有任务记录的任务不能删除
     */
    @Transactional(rollbackFor = Exception.class)
    public long deleteTask(Integer taskId) {
        for (TaskRecordService<?> recordService : ApplicationContextUtils.getBeans(TaskRecordService.class).values()) {
            ActivityAssert.notExistTaskRecord(recordService.exists(TaskRecordPo::getTaskId, taskId));
        }
        return this.delete(taskId);
    }

    /**
     * 查询用户当前周期内已完成的活动任务
     */
    public List<Integer> findFinishTasks(int activityId, long userId, List<Task> tasks) {
        if (CollectionUtils.isEmpty(tasks)) {
            return Collections.emptyList();
        }
        List<Integer> ids = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            if (isFinishTask(userId, activityId, task)) {
                ids.add(task.getId());
            }
        }
        return ids;
    }

    /**
     * 当前周期内用户是否已完成任务
     */
    public boolean isFinishTask(long userId, int activityId, Task task) {
        Integer limit = null;
        LocalDateTime startTime = null;
        if (null != task.getTaskPeriod()) {
            startTime = task.getTaskPeriod().getStartTime();
            limit = task.getTaskPeriod().getLimit();
        }
        return Constants.ZERO == taskExecutorFactory.get(task.getTaskType())
                .getTaskNumber(activityId, task.getId(), startTime, limit, userId);
    }

    @Autowired
    public void setTaskExecutorFactory(TaskExecutorFactory taskExecutorFactory) {
        this.taskExecutorFactory = taskExecutorFactory;
    }
}
