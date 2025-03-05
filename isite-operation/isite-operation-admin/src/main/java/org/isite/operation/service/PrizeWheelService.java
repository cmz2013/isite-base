package org.isite.operation.service;

import org.apache.commons.lang3.BooleanUtils;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.schedule.ProbabilityScheduler;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.prize.PrizeGiverFactory;
import org.isite.operation.support.constants.CacheKeys;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeWheelProperty;
import org.isite.operation.task.IdempotentKey;
import org.isite.operation.task.PrizeTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeWheelService {
    private PrizeTaskExecutor prizeTaskExecutor;
    private PrizeGiverFactory prizeGiverFactory;
    private PrizeRecordService prizeRecordService;
    private PrizeService prizeService;

    /**
     * 抽取奖品
     */
    @Synchronized(locks = @Lock(name = CacheKeys.LOCK_ACTIVITY_USER, keys = {"#activity.id", "#userId"}))
    public Prize drawPrize(Activity activity, long userId) {
        PrizeRecordPo recordPo = prizeRecordService.getNotReceive(activity.getId(), null, userId);
        if (null == recordPo) {
            recordPo = addPrizeRecord(userId, activity);
        }
        Assert.notNull(recordPo, MessageUtils.getMessage("draws.number.zero", "the number of draws is 0"));
        Prize prize = VoUtils.get(activity.getPrizes(), recordPo.getPrizeId());
        if (null == prize) {
            List<Prize> prizes = prizeService.filterPrizes(activity.getPrizes());
            Assert.notEmpty(prizes, MessageUtils.getMessage("prize.notInventory", "there are no more prizes"));
            prize = ProbabilityScheduler.choose(prizes, Prize::getProbability);
        }
        if (null != prize) {
            prizeGiverFactory.get(prize.getPrizeType()).execute(activity, prize, recordPo);
        }
        return prize;
    }

    /**
     * 抽奖之前增送抽奖次数，避免送了抽奖次数但大多数人不抽奖，从而产生大量垃圾数据
     */
    private PrizeRecordPo addPrizeRecord(Long userId, Activity activity) {
        PrizeWheelProperty activityProperty = TypeUtils.cast(activity.getProperty());
        if (BooleanUtils.isNotTrue(activityProperty.getFreeLottery())) {
            return null;
        }
        Integer limit = null;
        LocalDateTime periodStartTime = null;
        if (null != activityProperty.getTaskPeriod()) {
            periodStartTime = activityProperty.getTaskPeriod().getStartTime();
            limit = activityProperty.getTaskPeriod().getLimit();
        }
        long taskNumber = prizeTaskExecutor.getTaskNumber(
                activity.getId(), Constants.ZERO, periodStartTime, limit, userId);
        if (taskNumber > Constants.ZERO) {
            PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
            prizeRecordPo.setTaskId(Constants.ZERO);
            prizeRecordPo.setReceiveStatus(Boolean.FALSE);
            prizeRecordPo.setLockStatus(Boolean.FALSE);
            prizeRecordPo.setUserId(userId);
            prizeRecordPo.setObjectType(ObjectType.OPERATION_ACTIVITY);
            prizeRecordPo.setObjectValue(String.valueOf(activity.getId()));
            prizeRecordPo.setActivityId(activity.getId());
            prizeRecordPo.setActivityPid(activity.getPid());
            prizeRecordPo.setFinishTime(LocalDateTime.now());
            prizeRecordPo.setIdempotentKey(IdempotentKey.toValue(
                    activity.getId(), Constants.ZERO, periodStartTime, userId, taskNumber));
            prizeRecordService.insert(prizeRecordPo);
            return prizeRecordPo;
        }
        return null;
    }

    @Autowired
    public void setPrizeTaskExecutor(PrizeTaskExecutor prizeTaskExecutor) {
        this.prizeTaskExecutor = prizeTaskExecutor;
    }

    @Autowired
    public void setPrizeGiverFactory(PrizeGiverFactory prizeGiverFactory) {
        this.prizeGiverFactory = prizeGiverFactory;
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setPrizeService(PrizeService prizeService) {
        this.prizeService = prizeService;
    }
}
