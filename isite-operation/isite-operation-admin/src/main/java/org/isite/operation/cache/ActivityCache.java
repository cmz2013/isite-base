package org.isite.operation.cache;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.isite.commons.cloud.data.enums.TerminalType;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.operation.converter.ActivityConverter;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeService;
import org.isite.operation.service.TaskService;
import org.isite.operation.service.WebpageService;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
/**
 * @Description 运营活动缓存（只缓存已上架的活动）
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class ActivityCache {
    /**
     * 运营任务Service
     */
    private TaskService taskService;
    /**
     * 运营奖品Service
     */
    private PrizeService prizeService;
    /**
     * 运营活动Service
     */
    private ActivityService activityService;
    /**
     * 活动网页Service
     */
    private WebpageService webpageService;
    /**
     * 操作redis缓存
     */
    private StringRedisTemplate redisTemplate;

    /**
     * 根据行为类型查询已上架的运营活动ID
     */
    @Cached(name = CacheKeys.ACTIVITY_IDS_EVENT_PREFIX, key = "#eventType.code", expire = Constants.DAY_SECOND)
    public List<Integer> findActivityIds(EventType eventType) {
        return activityService.findEnabledActivityIds(eventType);
    }

    @Cached(name = CacheKeys.ACTIVITY_PREFIX, key = "#activityId",
            cacheType = CacheType.BOTH, expire = Constants.DAY_SECOND, localExpire = Constants.MINUTE_SECOND)
    public Activity getActivity(int activityId) {
        ActivityPo activityPo = activityService.getActivity(activityId, ActiveStatus.ENABLED);
        Assert.notNull(activityPo, MessageUtils.getMessage("activity.notFound", "activity not found"));
        return ActivityConverter.toActivity(activityPo, taskService.findList(TaskPo::getActivityId, activityId),
                        prizeService.findList(PrizePo::getActivityId, activityId));
    }

    /**
     * 更新已锁定的库存
     * @param prize 是 activity.prizes 集合中的元素
     */
    @CacheUpdate(name = CacheKeys.ACTIVITY_PREFIX, key = "#activity.id", value = "#activity")
    public void decrLockInventory(Activity activity, Prize prize) {
        prize.setLockInventory(prizeService.decrLockInventory(prize.getId(), prize.getLockInventory()));
    }

    /**
     * 活动网页模板实例化以后使用redis缓存
     * @param activity 活动数据
     * @param terminalType 用户终端类型
     * @return 活动网页html代码
     */
    @Cached(name = CacheKeys.WEBPAGE_ACTIVITY_TERMINAL_PREFIX,
            key = "#activity.id" + Constants.COLON + "#terminalType.code", expire = Constants.DAY_SECOND)
    public String getWebpage(Activity activity, TerminalType terminalType) {
        return webpageService.getWebpage(activity, terminalType);
    }

    /**
     * 更新已消耗库存
     * @param prize 是 activity.prizes 集合中的元素
     */
    @CacheUpdate(name = CacheKeys.ACTIVITY_PREFIX, key = "#activity.id", value = "#activity")
    public void incrConsumeInventory(Activity activity, Prize prize) {
        prize.setConsumeInventory(prizeService.incrConsumeInventory(prize.getId(), prize.getConsumeInventory()));
    }

    /**
     * 更新活动状态为下架
     */
    @Transactional(rollbackFor = Exception.class)
    public int disableActivity(Activity activity) {
        int count = activityService.updateById(activity.getId(), ActivityPo::getStatus, ActiveStatus.DISABLED);
        if (count > Constants.ZERO) {
            List<String> keys = new ArrayList<>();
            keys.add(CacheKeys.ACTIVITY_PREFIX + activity.getId());
            keys.add(CacheKeys.WEBPAGE_ACTIVITY_TERMINAL_PREFIX + activity.getId() + Constants.COLON + TerminalType.WEB.getCode());
            keys.add(CacheKeys.WEBPAGE_ACTIVITY_TERMINAL_PREFIX + activity.getId() + Constants.COLON + TerminalType.APP.getCode());
            activity.getTasks().stream().map(task -> task.getTaskType().getEventType())
                    .distinct().forEach(event -> keys.add(CacheKeys.ACTIVITY_IDS_EVENT_PREFIX + event.getCode()));
            redisTemplate.delete(keys);
        }
        return count;
    }

    /**
     * 更新活动状态为上架
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer enableActivity(int activityId) {
        int count = activityService.updateById(activityId, ActivityPo::getStatus, ActiveStatus.ENABLED);
        if (count > Constants.ZERO) {
            List<String> keys = new ArrayList<>();
            taskService.findList(TaskPo::getActivityId, activityId).stream().map(task -> task.getTaskType().getEventType())
                    .distinct().forEach(event -> keys.add(CacheKeys.ACTIVITY_IDS_EVENT_PREFIX + event.getCode()));
            redisTemplate.delete(keys);
        }
        return count;
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
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setWebpageService(WebpageService webpageService) {
        this.webpageService = webpageService;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
