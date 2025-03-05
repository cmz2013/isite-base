package org.isite.operation.service;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.operation.cache.ActivityCache;
import org.isite.operation.support.enums.EventType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @Description 进行中的活动Service
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class OngoingActivityService {
    private ActivityCache activityCache;

    /**
     * 查询进行中的活动奖品
     */
    public Prize getOngoingPrize(Integer activityId, Integer prizeId) {
        Activity activity = getOngoingActivity(activityId);
        if (null == activity) {
            return null;
        }
        return VoUtils.get(activity.getPrizes(), prizeId);
    }

    /**
     * 根据ID查询进行中的活动
     */
    public Activity getOngoingActivity(Integer activityId) {
        Activity activity = activityCache.getActivity(activityId);
        if (null != activity) {
            LocalDateTime currTime = LocalDateTime.now();
            if (currTime.isAfter(activity.getStartTime()) && currTime.isBefore(activity.getEndTime())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 根据行为类型查询进行中的活动
     */
    public List<Activity> findOngoingActivities(EventType eventType) {
        List<Integer> activityIds = activityCache.findActivityIds(eventType);
        if (CollectionUtils.isEmpty(activityIds)) {
            return Collections.emptyList();
        }
        List<Activity> activityList = new ArrayList<>(activityIds.size());
        activityIds.forEach(activityId -> {
            Activity activity = getOngoingActivity(activityId);
            if (null != activity) {
                activityList.add(activity);
            }
        });
        return activityList;
    }

    @Autowired
    public void setActivityCache(ActivityCache activityCache) {
        this.activityCache = activityCache;
    }
}
