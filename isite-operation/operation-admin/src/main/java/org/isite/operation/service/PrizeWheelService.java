package org.isite.operation.service;

import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.data.vo.PrizeWheelProperty;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.prize.PrizeGiverFactory;
import org.isite.operation.task.PrizeTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notEmpty;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.schedule.ProbabilityScheduler.choose;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.lang.utils.VoUtils.get;
import static org.isite.misc.data.enums.ObjectType.OPERATION_ACTIVITY;
import static org.isite.operation.data.constants.CacheKey.LOCK_WHEEL_USER;
import static org.isite.operation.task.IdempotentKey.toValue;

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
    @Synchronized(locks = @Lock(name = LOCK_WHEEL_USER, keys = {"#activity", "#userId"}))
    public Prize drawPrize(Activity activity, long userId) {
        PrizeRecordPo recordPo = prizeRecordService.getNotReceive(activity.getId(), null, userId);
        if (null == recordPo) {
            recordPo = addPrizeRecord(userId, activity);
        }
        notNull(recordPo, getMessage("draws.number.zero", "the number of draws is 0"));
        Prize prize = get(activity.getPrizes(), recordPo.getPrizeId());
        if (null == prize) {
            List<Prize> prizes = prizeService.filterPrizes(activity.getPrizes());
            notEmpty(prizes, getMessage("prize.notInventory", "there are no more prizes"));
            prize = choose(prizes, Prize::getProbability);
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
        PrizeWheelProperty activityProperty = cast(activity.getProperty());
        if (isNotTrue(activityProperty.getFreeLottery())) {
            return null;
        }
        Integer limit = null;
        Date periodStartTime = null;
        if (null != activityProperty.getTaskPeriod()) {
            periodStartTime = activityProperty.getTaskPeriod().getStartTime();
            limit = activityProperty.getTaskPeriod().getLimit();
        }
        long taskNumber = prizeTaskExecutor.getTaskNumber(
                activity.getId(), ZERO, periodStartTime, limit, userId);
        if (taskNumber > ZERO) {
            PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
            prizeRecordPo.setTaskId(ZERO);
            prizeRecordPo.setReceiveStatus(FALSE);
            prizeRecordPo.setLockStatus(FALSE);
            prizeRecordPo.setUserId(userId);
            prizeRecordPo.setObjectType(OPERATION_ACTIVITY);
            prizeRecordPo.setObjectValue(valueOf(activity.getId()));
            prizeRecordPo.setActivityId(activity.getId());
            prizeRecordPo.setActivityPid(activity.getPid());
            prizeRecordPo.setFinishTime(new Date(currentTimeMillis()));
            prizeRecordPo.setIdempotentKey(toValue(
                    activity.getId(), ZERO, periodStartTime, userId, taskNumber));
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
