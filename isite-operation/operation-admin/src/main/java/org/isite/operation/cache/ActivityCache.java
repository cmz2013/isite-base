package org.isite.operation.cache;

import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import org.isite.commons.cloud.enums.TerminalType;
import org.isite.commons.web.mq.Message;
import org.isite.commons.web.mq.Publisher;
import org.isite.operation.data.enums.EventType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.mq.WebpageProducer;
import org.isite.operation.po.ActivityPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.TaskPo;
import org.isite.operation.service.ActivityService;
import org.isite.operation.service.PrizeService;
import org.isite.operation.service.TaskService;
import org.isite.operation.service.WebpageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.alicp.jetcache.anno.CacheType.BOTH;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.data.Constants.COLON;
import static org.isite.commons.lang.data.Constants.DAY_SECONDS;
import static org.isite.commons.lang.data.Constants.MINUTE_SECONDS;
import static org.isite.commons.lang.enums.SwitchStatus.DISABLED;
import static org.isite.commons.lang.enums.SwitchStatus.ENABLED;
import static org.isite.operation.converter.ActivityConverter.toActivity;
import static org.isite.operation.data.constants.CacheKey.ACTIVITY_IDS_EVENT_PREFIX;
import static org.isite.operation.data.constants.CacheKey.ACTIVITY_PREFIX;
import static org.isite.operation.data.constants.CacheKey.WEBPAGE_ACTIVITY_USERAGENT_PREFIX;
import static org.isite.operation.data.constants.OperationConstants.QUEUE_OPERATION_EVENT;

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
    @Cached(name = ACTIVITY_IDS_EVENT_PREFIX, key = "#eventType.code", expire = DAY_SECONDS)
    public List<Integer> findActivityIds(EventType eventType) {
        return activityService.findActivityIds(eventType);
    }

    @Cached(name = ACTIVITY_PREFIX, key = "#activityId", cacheType = BOTH, expire = DAY_SECONDS, localExpire = MINUTE_SECONDS)
    public Activity getActivity(Integer activityId) {
        ActivityPo activityPo = activityService.getActivity(activityId, ENABLED);
        notNull(activityPo, getMessage("activity.notFound", "activity not found"));
        return toActivity(activityPo, taskService.findList(TaskPo::getActivityId, activityId),
                        prizeService.findList(PrizePo::getActivityId, activityId));
    }

    /**
     * 更新已锁定的库存
     * @param prize 是 activity.prizes 集合中的元素
     */
    @CacheUpdate(name = ACTIVITY_PREFIX, key = "#activity.id", value = "#activity")
    public void decrLockInventory(Activity activity, Prize prize) {
        prize.setLockInventory(prizeService.decrLockInventory(prize.getId(), prize.getLockInventory()));
    }

    /**
     * 活动网页模板实例化以后使用redis缓存
     * @param activity 活动数据
     * @param terminalType 用户终端类型
     * @return 活动网页html代码
     */
    @Publisher(messages = @Message(queues = QUEUE_OPERATION_EVENT, producer = WebpageProducer.class))
    @Cached(name = WEBPAGE_ACTIVITY_USERAGENT_PREFIX, key = "#activity.id" + COLON + "#userAgent.code", expire = DAY_SECONDS)
    public String getWebpage(Activity activity, TerminalType terminalType) {
        return webpageService.getWebpage(activity, terminalType);
    }

    /**
     * 更新已消耗库存
     * @param prize 是 activity.prizes 集合中的元素
     */
    @CacheUpdate(name = ACTIVITY_PREFIX, key = "#activity.id", value = "#activity")
    public void incrConsumeInventory(Activity activity, Prize prize) {
        prize.setConsumeInventory(prizeService.incrConsumeInventory(prize.getId(), prize.getConsumeInventory()));
    }

    /**
     * 更新活动为下架状态
     */
    public int updateStatus(Activity activity) {
        List<String> keys = new ArrayList<>();
        keys.add(ACTIVITY_PREFIX + activity.getId());
        keys.add(WEBPAGE_ACTIVITY_USERAGENT_PREFIX + activity.getId() + COLON + TRUE);
        keys.add(WEBPAGE_ACTIVITY_USERAGENT_PREFIX + activity.getId() + COLON + FALSE);
        activity.getTasks().stream().map(task -> task.getTaskType().getEventType())
                .distinct().forEach(event -> keys.add(ACTIVITY_IDS_EVENT_PREFIX + event.getCode()));
        redisTemplate.delete(keys);
        return activityService.updateStatus(activity.getId(), DISABLED);
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
