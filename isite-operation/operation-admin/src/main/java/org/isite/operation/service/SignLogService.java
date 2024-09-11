package org.isite.operation.service;

import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.mybatis.service.PoService;
import org.isite.operation.mapper.SignLogMapper;
import org.isite.operation.po.SignLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.enums.ChronoUnit.DAY;
import static org.isite.commons.lang.utils.DateUtils.getStartTimeOfDay;
import static org.isite.operation.support.constants.CacheKey.LOCK_SIGN_USER;

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
}
