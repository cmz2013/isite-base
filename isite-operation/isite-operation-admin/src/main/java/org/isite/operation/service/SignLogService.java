package org.isite.operation.service;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.DateUtils;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.mybatis.service.PoService;
import org.isite.operation.mapper.SignLogMapper;
import org.isite.operation.po.SignLogPo;
import org.isite.operation.support.constants.CacheKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    @Synchronized(locks = {@Lock(name = CacheKeys.LOCK_USER_SIGN, keys = "#userId")})
    public SignLogPo saveSignLog(long userId) {
        int totalCount = Constants.ONE;
        int continuousCount = Constants.ONE;
        SignLogPo lastSignLog = getLastSignLog(userId);
        if (null != lastSignLog) {
            Assert.isTrue(DateUtils.startOfDay().isAfter(lastSignLog.getSignTime()),
                    MessageUtils.getMessage("signed.inToday", "you has been signed in today"));
            continuousCount += getContinuousCount(lastSignLog);
            totalCount += lastSignLog.getTotalCount();
        }
        SignLogPo signLogPo = new SignLogPo();
        signLogPo.setUserId(userId);
        signLogPo.setSignTime(LocalDateTime.now());
        signLogPo.setContinuousCount(continuousCount);
        signLogPo.setTotalCount(totalCount);
        this.insert(signLogPo);
        return signLogPo;
    }

    /**
     * 根据最近一次签到信息获取已连续签到天数
     */
    private int getContinuousCount(SignLogPo latestSignLog) {
        if (latestSignLog.getSignTime().isAfter(DateUtils.startOfDay(LocalDateTime.now().minusDays(Constants.ONE)))) {
            return latestSignLog.getContinuousCount();
        }
        return Constants.ZERO;
    }
}
