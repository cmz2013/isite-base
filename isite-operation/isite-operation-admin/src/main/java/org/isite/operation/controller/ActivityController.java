package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.commons.lang.json.JsonField;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.activity.ActivityAssert;
import org.isite.operation.cache.ActivityCache;
import org.isite.operation.converter.ActivityConverter;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeService;
import org.isite.operation.service.TaskRecordService;
import org.isite.operation.service.TaskService;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.dto.ActivityDto;
import org.isite.operation.support.dto.ActivityQuery;
import org.isite.operation.support.enums.ActivityTheme;
import org.isite.operation.support.vo.Activity;
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
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ActivityController extends BaseController {
    public static final String KEY_ACTIVITY_NOT_FOUND = "activity.notFound";
    public static final String VALUE_ACTIVITY_NOT_FOUND = "activity not found";
    private TaskService taskService;
    private ActivityCache activityCache;
    private PrizeService prizeService;
    private ActivityService activityService;

    /**
     * 查询活动信息（用于管理后台查询数据）
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/activities")
    public PageResult<Activity> findPage(PageRequest<ActivityQuery> request) {
        try (Page<ActivityPo> page = activityService.findPage(PageQueryConverter.toPageQuery(request, ActivityPo::new))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), Activity::new), page.getTotal());
        }
    }

    @PostMapping(OperationUrls.URL_OPERATION + "/activity")
    public Result<Integer> addActivity(@RequestBody @Validated(Add.class) ActivityDto activityDto) {
        Assert.isTrue(activityService.isRoot(activityDto.getPid()) ||
                        activityService.isRoot(activityService.get(activityDto.getPid()).getPid()),
                MessageUtils.getMessage("activity.twoLevels", "sub-activities can only have two levels"));
        return toResult(activityService.addActivity(ActivityConverter.toActivityPo(activityDto)));
    }

    @PutMapping(OperationUrls.URL_OPERATION + "/activity")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityDto.id"))
    public Result<Integer> editActivity(@Validated(Update.class) @RequestBody ActivityDto activityDto) {
        ActivityAssert.notOnline(activityService.get(activityDto.getId()).getStatus());
        return toResult(activityService.updateById(DataConverter.convert(activityDto, ActivityPo::new)));
    }

    @DeleteMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId"))
    public Result<Integer> deleteActivity(@PathVariable("activityId") Integer activityId) {
        ActivityPo activityPo = activityService.get(activityId);
        ActivityAssert.notOnline(activityPo.getStatus());
        if (activityService.isRoot(activityPo.getPid())) {
            Assert.isFalse(activityService.exists(ActivityPo::getPid, activityPo.getId()),
                    MessageUtils.getMessage("parentActivity.notDelete", "there are sub-activities"));
        }
        ApplicationContextUtils.getBeans(TaskRecordService.class).values().forEach(
                taskRecordService -> ActivityAssert.notExistTaskRecord(taskRecordService.exists(activityId)));
        return toResult(activityService.deleteActivity(activityId));
    }

    /**
     * 获取活动主题
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/activity/themes")
    public Result<ActivityTheme[]> getThemes() {
        return toResult(ActivityTheme.values());
    }

    /**
     * 获取运营活动自定义属性
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/activity/{theme}/properties")
    public Result<List<JsonField>> getProperties(@PathVariable("theme")ActivityTheme theme) {
        return toResult(Reflection.toJsonFields(theme.getPropertyClass()));
    }

    /**
     * 查询在线的活动信息（不需要登录就可以访问）
     */
    @GetMapping(UrlConstants.URL_API + OperationUrls.URL_OPERATION + "/activity/{activityId}")
    public Result<Activity> getOnlineActivity(@PathVariable("activityId") Integer activityId) {
        return toResult(activityCache.getActivity(activityId));
    }

    /**
     * 查询活动信息（从DB查询数据，主要用于管理员登录后台查询数据）
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}")
    public Result<Activity> getActivity(@PathVariable("activityId") Integer activityId) {
        ActivityPo activityPo = activityService.get(activityId);
        return toResult(null == activityPo ? null :
                ActivityConverter.toActivity(activityPo, taskService.findList(TaskPo::getActivityId, activityId),
                        prizeService.findList(PrizePo::getActivityId, activityId)));
    }

    /**
     * 活动上下架操作。上架的活动个数不能超过1000
     */
    @PutMapping(OperationUrls.URL_OPERATION + "/activity/{activityId}/status/{status}")
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY, keys = "#activityId", condition = "#status.code==1"))
    public Result<Integer> updateStatus(@PathVariable("activityId") Integer activityId,
                                        @PathVariable("status") ActiveStatus status) {
        if (ActiveStatus.DISABLED.equals(status)) {
            return toResult(activityCache.disableActivity(activityCache.getActivity(activityId)));
        } else {
            Assert.isTrue(Constants.THOUSAND > activityService.count(ActivityPo::getStatus, ActiveStatus.ENABLED),
                    MessageUtils.getMessage("activity.total.error", "the total of listed activities cannot exceed 1000"));
            return toResult(activityCache.enableActivity(activityId));
        }
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setActivityCache(ActivityCache activityCache) {
        this.activityCache = activityCache;
    }
}
