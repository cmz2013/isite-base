package org.isite.operation.service;

import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.mybatis.service.PoService;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.SignScoreProperty;
import org.isite.operation.data.vo.SignScoreReward;
import org.isite.operation.mapper.SignLogMapper;
import org.isite.operation.po.SignLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfDay;
import static org.isite.operation.data.constants.CacheKey.LOCK_SIGN_USER;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class SignLogService extends PoService<SignLogPo, Long> {

    @Autowired
    public SignLogService(SignLogMapper signLogMapper) {
        super(signLogMapper);
    }

    /**
     * 获取用户最近一次的签到信息
     */
    public SignLogPo getLastSignLog(long userId) {
        return ((SignLogMapper) getMapper()).selectLastSignLog(userId);
    }

    /**
     * 完成每日签到
     */
    @Transactional(rollbackFor = Exception.class)
    @Synchronized(locks = {@Lock(name = LOCK_SIGN_USER, keys = "#userId")})
    public SignLogPo saveSignLog(long userId) {
        int totalCount =  ONE;
        int continuousCount =  ONE;
        SignLogPo lastSignLog = getLastSignLog(userId);
        if (null != lastSignLog) {
            isTrue(getStartTimeOfDay().getTime() > lastSignLog.getSignTime().getTime(),
                    getMessage("signed.inToday", "you has been signed in today"));
            continuousCount += getContinuousCount(lastSignLog);
            totalCount += lastSignLog.getTotalCount();
        }
        SignLogPo signLogPo = new SignLogPo();
        signLogPo.setUserId(userId);
        signLogPo.setSignTime(new Date(currentTimeMillis()));
        signLogPo.setContinuousCount(continuousCount);
        signLogPo.setTotalCount(totalCount);
        this.insert(signLogPo);
        return signLogPo;
    }

    /**
     * 获取已连续签到天数
     */
    private int getContinuousCount(SignLogPo latestSignLog) {
        if (latestSignLog.getSignTime().getTime() >=
                getStartTimeOfDay(new Date(currentTimeMillis() - DAY.getMillis())).getTime()) {
            return latestSignLog.getContinuousCount();
        }
        return ZERO;
    }

    /**
     * 根据用户连续签到获取任务奖励
     * @param signScoreProperty 任务属性
     * @param continuousCount 连续签到天数
     * @return 任务奖励
     */
    public Reward getReward(SignScoreProperty signScoreProperty, int continuousCount) {
        if (null == signScoreProperty || isEmpty(signScoreProperty.getRewards())) {
            return null;
        }
        SignScoreReward target = null;
        for (SignScoreReward reward : signScoreProperty.getRewards()) {
            notNull(reward.getContinuousCount(), "reward.continuousCount cannot be null");
            if (continuousCount == reward.getContinuousCount()) {
                return reward;
            } else if (continuousCount > reward.getContinuousCount()) {
                if (null == target || target.getContinuousCount() < reward.getContinuousCount()) {
                    target =  reward;
                }
            }
        }
        return target;
    }
}
