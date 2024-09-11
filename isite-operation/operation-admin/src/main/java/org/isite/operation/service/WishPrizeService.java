package org.isite.operation.service;

import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.prize.PrizeGiverFactory;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.WishPrizeProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.cloud.utils.VoUtils.get;
import static org.isite.commons.lang.Assert.isNull;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.misc.data.enums.ObjectType.USER;
import static org.isite.operation.converter.PrizeRecordConverter.toPrizeRecordPo;
import static org.isite.operation.converter.PrizeRecordConverter.toPrizeRecordSelectivePo;
import static org.isite.operation.task.IdempotentKey.toValue;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class WishPrizeService {

    private PrizeGiverFactory prizeGiverFactory;
    private PrizeRecordService prizeRecordService;
    private ScoreRecordService scoreRecordService;

    /**
     * 添加心愿礼品
     */
    public int addWishPrize(Activity activity, Prize prize, long userId) {
        isNull(prizeRecordService.getNotReceive(prize.getActivityId(), null, userId),
                getMessage("wish.exists", "you have an wish"));
        return addPrizeRecord(activity, prize, userId);
    }

    /**
     * 创建心愿记录（奖品记录，待抽奖）
     */
    private int addPrizeRecord(Activity activity, Prize prize, long userId) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        prizeRecordPo.setUserId(userId);
        prizeRecordPo.setObjectType(USER);
        prizeRecordPo.setObjectValue(valueOf(userId));
        prizeRecordPo.setReceiveStatus(FALSE);
        prizeRecordPo.setLockStatus(FALSE);
        prizeRecordPo.setActivityId(activity.getId());
        prizeRecordPo.setActivityPid(activity.getPid());
        prizeRecordPo.setTaskId(ZERO);
        prizeRecordPo.setFinishTime(new Date(currentTimeMillis()));
        toPrizeRecordPo(prizeRecordPo, prize);
        prizeRecordPo.setIdempotentKey(toValue(
                activity.getId(), ZERO, null, userId,
                prizeRecordService.count(toPrizeRecordSelectivePo(activity.getId(), ZERO, userId))));
        return prizeRecordService.insert(prizeRecordPo);
    }

    /**
     * 变更心愿礼品
     */
    public long updateWishPrize(int activityId, long userId, Prize prize) {
        PrizeRecordPo prizeRecordPo = prizeRecordService.getNotReceive(activityId, null, userId);
        notNull(prizeRecordPo, getMessage("wish.notFound", "wish not found"));
        return prizeRecordService.updatePrize(prizeRecordPo.getId(), prize);
    }

    /**
     * 实现心愿（兑换心愿礼品）
     */
    @Transactional(rollbackFor = Exception.class)
    public PrizeRecordPo receiveWishPrize(Activity activity, int prizeId, long userId) {
        Prize prize = get(activity.getPrizes(), prizeId);
        notNull(prize, getMessage("prize.notFound", "prize not found"));
        PrizeRecordPo recordPo = prizeRecordService.getNotReceive(activity.getId(), prizeId, userId);
        notNull(recordPo, getMessage("wish.notFound", "wish not found"));
        long availableScore = scoreRecordService.sumActivityScore(activity.getId(), userId);
        WishPrizeProperty activityProperty = cast(activity.getProperty());
        isTrue(availableScore >= activityProperty.getFullScore(),
                getMessage("wish.prize.notReceive", "you don't have enough score to receive this prize"));
        scoreRecordService.useActivityScore(activity.getId(), userId, activityProperty.getFullScore());
        prizeGiverFactory.get(prize.getPrizeType()).execute(activity, prize, recordPo);
        return recordPo;
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }

    @Autowired
    public void setPrizeGiverFactory(PrizeGiverFactory prizeGiverFactory) {
        this.prizeGiverFactory = prizeGiverFactory;
    }
}
