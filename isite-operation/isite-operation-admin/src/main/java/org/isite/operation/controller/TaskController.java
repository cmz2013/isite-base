package org.isite.operation.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.json.JsonField;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.IllegalParameterError;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.cache.ActivityCache;
import org.isite.operation.po.TaskPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.TaskService;
import org.isite.operation.support.dto.TaskPostDto;
import org.isite.operation.support.dto.TaskPutDto;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.Reflection.toJsonFields;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.activity.ActivityAssert.notOnline;
import static org.isite.operation.converter.TaskConverter.toTaskPo;
import static org.isite.operation.support.constants.CacheKeys.LOCK_ACTIVITY;
import static org.isite.operation.support.constants.OperationUrls.URL_OPERATION;
import static org.isite.operation.support.enums.TaskType.values;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TaskController extends BaseController {

    private TaskService taskService;
    private ActivityService activityService;
    private ActivityCache activityCache;

    /**
     * 一个活动的任务个数不能超过1000（用于管理后台查询数据）
     */
    @GetMapping(URL_OPERATION + "/activity/{activityId}/tasks")
    public Result<List<Task>> findTasks(@PathVariable("activityId") Integer activityId) {
        return toResult(convert(taskService.findList(TaskPo::getActivityId, activityId), Task::new));
    }

    /**
     * 查询当前周期内已完成的活动任务（用于用户查询数据）
     */
    @GetMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/tasks/finish")
    public Result<List<Integer>> findFinishTasks(@PathVariable("activityId") Integer activityId) {
        return toResult(taskService.findFinishTasks(activityId, getUserId(), activityCache.getActivity(activityId).getTasks()));
    }

    /**
     * 获取运营任务自定义属性
     */
    @GetMapping(URL_OPERATION + "/task/{type}/properties")
    public Result<List<JsonField>> getProperties(@PathVariable("type") TaskType type) {
        return toResult(toJsonFields(type.getPropertyClass()));
    }

    /**
     * 更新运营任务，不能变更活动ID
     */
    @PutMapping(URL_OPERATION + "/activity/{activityId}/task")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> updateTask(@PathVariable("activityId") Integer activityId,
                                      @Validated @RequestBody TaskPutDto taskPutDto) {
        notOnline(activityService.get(activityId).getStatus());
        isTrue(taskService.get(taskPutDto.getId()).getActivityId().equals(activityId), new IllegalParameterError());
        isTrue(THOUSAND > taskService.count(TaskPo::getActivityId, activityId),
                getMessage("task.total.error", "the total of tasks cannot exceed 1000"));
        return toResult(taskService.updateSelectiveById(convert(taskPutDto, TaskPo::new)));
    }

    /**
     * 根据行为类型查询任务类型
     */
    @GetMapping(URL_OPERATION + "/task/{event}/types")
    public Result<List<TaskType>> getTaskTypes(@PathVariable("event") EventType event) {
        return toResult(values(event));
    }

    /**
     * 删除运营任务
     */
    @DeleteMapping(URL_OPERATION + "/activity/{activityId}/task/{taskId}")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Long> deleteTask(@PathVariable("activityId") Integer activityId,
                                   @PathVariable("taskId") Integer taskId) {
        notOnline(activityService.get(activityId).getStatus());
        isTrue(taskService.get(taskId).getActivityId().equals(activityId), new IllegalParameterError());
        return toResult(taskService.deleteTask(taskId));
    }

    /**
     * 新增运营任务
     */
    @PostMapping(URL_OPERATION + "/task")
    @Synchronized(locks = @Lock(name = LOCK_ACTIVITY, keys = "#taskPostDto.activityId"))
    public Result<Integer> addTask(@Validated @RequestBody TaskPostDto taskPostDto) {
        notOnline(activityService.get(taskPostDto.getActivityId()).getStatus());
        return toResult(taskService.insert(toTaskPo(taskPostDto)));
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setActivityCache(ActivityCache activityCache) {
        this.activityCache = activityCache;
    }
}
