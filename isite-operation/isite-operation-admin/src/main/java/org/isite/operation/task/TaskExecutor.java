package org.isite.operation.task;

import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.factory.Strategy;
import org.isite.operation.po.TaskObjectPo;
import org.isite.operation.po.TaskRecordPo;
import org.isite.operation.service.TaskObjectService;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.Task;
import org.isite.operation.support.vo.TaskProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

import static java.lang.Boolean.TRUE;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.operation.task.IdempotentKey.toValue;

/**
 * @Description 任务执行接口，用于处理行为消息
 * @param <P> 任务记录PO Class
 * 运营任务执行主要分为两个阶段：
 * 阶段一：用户完成业务操作发送行为消息；
 * 阶段二：处理行为消息赠送任务奖励（一个任务最多只能发放一个奖励）
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public abstract class TaskExecutor<P extends TaskRecordPo> implements Strategy<TaskType> {

    private TaskObjectService taskObjectService;

    /**
     * 保存任务完成记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void execute(Activity activity, Task task, EventDto eventDto) {
        if (!checkTaskProperty(task.getProperty(), eventDto) && checkTaskObject(task, eventDto)) {
            return;
        }
        Integer limit = null;
        LocalDateTime startTime = null;
        if (null != task.getTaskPeriod()) {
            startTime = task.getTaskPeriod().getStartTime();
            limit = task.getTaskPeriod().getLimit();
        }
        long taskNumber = getTaskNumber(activity.getId(), task.getId(), startTime, limit, eventDto);
        if (taskNumber > ZERO) {
            P taskRecord = createTaskRecord(eventDto, activity, task, startTime, taskNumber);
            if (null != taskRecord) {
                saveTaskRecord(activity, taskRecord, getReward(activity, task, eventDto));
            }
        }
    }

    /**
     * 校验任务自定义属性约束条件
     */
    protected boolean checkTaskProperty(TaskProperty<?> taskProperty, EventDto eventDto) {
        return TRUE;
    }

    /**
     * 校验任务对象约束条件
     */
    protected boolean checkTaskObject(Task task, EventDto eventDto) {
        TaskObjectPo taskObjectPo = new TaskObjectPo();
        taskObjectPo.setTaskId(task.getId());
        if (taskObjectService.count(taskObjectPo) > ZERO) {
            taskObjectPo.setObjectType(eventDto.getEventType().getObjectType());
            taskObjectPo.setObjectValue(eventDto.getObjectValue());
            return taskObjectService.count(taskObjectPo) > ZERO;
        }
        return TRUE;
    }

    /**
     * 创建任务参与记录
     */
    protected P createTaskRecord(EventDto eventDto, Activity activity,
                                 Task task, LocalDateTime periodStartTime, long taskNumber) {
        try {
            Class<P> rClass = getTaskRecordClass();
            P taskRecord = rClass.getDeclaredConstructor().newInstance();
            taskRecord.setTaskId(task.getId());
            taskRecord.setActivityPid(activity.getPid());
            taskRecord.setObjectType(eventDto.getEventType().getObjectType());
            taskRecord.setObjectValue(eventDto.getObjectValue());
            taskRecord.setFinishTime(new Date(currentTimeMillis()));
            taskRecord.setUserId(eventDto.getUserId());
            taskRecord.setActivityId(activity.getId());
            taskRecord.setRemark(task.getRemark());
            taskRecord.setIdempotentKey(toValue(
                    activity.getId(), task.getId(), periodStartTime, eventDto.getUserId(), taskNumber));
            return taskRecord;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected Class<P> getTaskRecordClass() {
        return cast(getGenericParameter(this.getClass(), TaskExecutor.class));
    }

    /**
     * 保存任务记录
     * @param activity 运营活动
     * @param taskRecord 是TaskRecordPo子类的实例，该子类扩展的属性，需要在TaskExecutor的实现类中完成赋值并保存
     * @param reward 任务奖励
     */
    protected abstract void saveTaskRecord(Activity activity, P taskRecord, Reward reward);

    /**
     * 选取运营任务奖励，一个任务最多只能发放一个奖励
     * @param activity 运营活动
     * @param task 运营任务
     * @param eventDto 行为参数
     * @return 任务奖励
     */
    protected Reward getReward(Activity activity, Task task, EventDto eventDto) {
        if (null == task.getProperty() || isEmpty(task.getProperty().getRewards())) {
            return null;
        }
        return task.getProperty().getRewards().get(ZERO);
    }

    /**
     * 获取任务号，即当前任务在当前任务周期内的执行次数，等于0不执行任务
     * @param activityId 活动ID
     * @param taskId 任务ID
     * @param periodStartTime 当前任务周期开始时间
     * @param limit 一个周期内可以重复完成任务的次数（可以空）
     * @param eventDto 行为参数
     * @return 任务号
     */
    protected long getTaskNumber(
            int activityId, int taskId, LocalDateTime periodStartTime, Integer limit, EventDto eventDto) {
        return getTaskNumber(activityId, taskId, periodStartTime, limit, eventDto.getUserId());
    }

    /**
     * 获取任务号，即当前任务在当前任务周期内的执行次数，等于0不执行任务
     * @param activityId 活动ID
     * @param taskId 任务ID
     * @param periodStartTime 当前任务周期开始时间
     * @param limit 一个周期内可以重复完成任务的次数（可以空）
     * @param userId 用户ID
     * @return 任务号
     */
    public long getTaskNumber(int activityId, int taskId, LocalDateTime periodStartTime, Integer limit, long userId) {
        long records = countTaskRecord(activityId, taskId, periodStartTime, userId);
        if (null != limit && limit > ZERO) {
            return limit > records ? records + ONE : ZERO;
        }
        //如果不配置任务周期频率，可以重复多次执行任务
        return records + ONE;
    }

    /**
     * 查询任务记录条数
     * @param activityId 活动ID
     * @param taskId 任务ID
     * @param startTime 当前任务周期开始时间
     * @param userId 用户ID
     * @return 任务记录条数
     */
    protected abstract long countTaskRecord(int activityId, int taskId, @Nullable LocalDateTime startTime, long userId);

    @Autowired
    public void setTaskObjectService(TaskObjectService taskObjectService) {
        this.taskObjectService = taskObjectService;
    }
}
