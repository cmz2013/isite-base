package org.isite.operation.service;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.converter.PrizeRecordConverter;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.prize.PrizeGiverFactory;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.WishPrizeProperty;
import org.isite.operation.task.IdempotentKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        Assert.isNull(prizeRecordService.getNotReceive(prize.getActivityId(), null, userId),
                MessageUtils.getMessage("wish.exists", "you have an wish"));
        return addPrizeRecord(activity, prize, userId);
    }

    /**
     * 创建心愿记录（奖品记录，待抽奖）
     */
    private int addPrizeRecord(Activity activity, Prize prize, long userId) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        prizeRecordPo.setUserId(userId);
        prizeRecordPo.setObjectType(ObjectType.USER);
        prizeRecordPo.setObjectValue(String.valueOf(userId));
        prizeRecordPo.setReceiveStatus(Boolean.FALSE);
        prizeRecordPo.setLockStatus(Boolean.FALSE);
        prizeRecordPo.setActivityId(activity.getId());
        prizeRecordPo.setActivityPid(activity.getPid());
        prizeRecordPo.setTaskId(Constants.ZERO);
        prizeRecordPo.setFinishTime(LocalDateTime.now());
        PrizeRecordConverter.toPrizeRecordPo(prizeRecordPo, prize);
        prizeRecordPo.setIdempotentKey(IdempotentKey.toValue(
                activity.getId(), Constants.ZERO, null, userId,
                prizeRecordService.count(PrizeRecordConverter.toPrizeRecordSelectivePo(activity.getId(), Constants.ZERO, userId))));
        return prizeRecordService.insert(prizeRecordPo);
    }

    /**
     * 变更心愿礼品
     */
    public long updateWishPrize(int activityId, long userId, Prize prize) {
        PrizeRecordPo prizeRecordPo = prizeRecordService.getNotReceive(activityId, null, userId);
        Assert.notNull(prizeRecordPo, MessageUtils.getMessage("wish.notFound", "wish not found"));
        return prizeRecordService.updatePrize(prizeRecordPo.getId(), prize);
    }

    /**
     * 实现心愿（兑换心愿礼品）
     */
    @Transactional(rollbackFor = Exception.class)
    public PrizeRecordPo receiveWishPrize(Activity activity, int prizeId, long userId) {
        Prize prize = VoUtils.get(activity.getPrizes(), prizeId);
        Assert.notNull(prize, MessageUtils.getMessage("prize.notFound", "prize not found"));
        PrizeRecordPo recordPo = prizeRecordService.getNotReceive(activity.getId(), prizeId, userId);
        Assert.notNull(recordPo, MessageUtils.getMessage("wish.notFound", "wish not found"));
        long availableScore = scoreRecordService.sumActivityScore(activity.getId(), userId);
        WishPrizeProperty activityProperty = TypeUtils.cast(activity.getProperty());
        Assert.isTrue(availableScore >= activityProperty.getFullScore(), MessageUtils.getMessage(
                "wish.prize.notReceive", "you don't have enough score to receive this prize"));
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
