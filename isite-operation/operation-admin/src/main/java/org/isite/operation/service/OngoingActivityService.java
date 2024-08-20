package org.isite.operation.service;

import org.isite.operation.cache.ActivityCache;
import org.isite.operation.data.enums.EventType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.utils.VoUtils.get;

/**
 * 进行中的活动Service
 * @author <font color='blue'>zhangcm</font>
 */
@Service
public class OngoingActivityService {

    private ActivityCache activityCache;

    /**
     * 查询进行中的活动奖品
     */
    public Prize getPrize(Integer activityId, Integer prizeId) {
        Activity activity = getActivity(activityId);
        if (null == activity) {
            return null;
        }
        return get(activity.getPrizes(), prizeId);
    }

    /**
     * 根据ID查询进行中的活动
     */
    public Activity getActivity(Integer activityId) {
        Activity activity = activityCache.getActivity(activityId);
        return inProgress(activity) ? activity : null;
    }

    /**
     * 校验活动是否进行中
     */
    public boolean inProgress(Activity activity) {
        if (null != activity) {
            long currTime = currentTimeMillis();
            return currTime >= activity.getStartTime().getTime() &&
                    currTime <= activity.getEndTime().getTime();
        }
        return FALSE;
    }

    /**
     * 根据行为类型查询进行中的活动
     */
    public List<Activity> findActivityList(EventType eventType) {
        return findActivityList(activityCache.findActivityIds(eventType));
    }

    /**
     * 根据ID查询进行中的活动
     */
    private List<Activity> findActivityList(List<Integer> activityIds) {
        if (isEmpty(activityIds)) {
            return emptyList();
        }
        List<Activity> activityList = new ArrayList<>(activityIds.size());
        activityIds.forEach(activityId -> {
            Activity activity = getActivity(activityId);
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
