package org.isite.operation.prize;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.commons.web.sync.Lock;
import org.isite.commons.web.sync.Synchronized;
import org.isite.operation.cache.ActivityCache;
import org.isite.operation.support.enums.PrizeType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.Boolean.TRUE;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.operation.support.constants.CacheKey.LOCK_PRIZE;
import static org.isite.operation.support.enums.PrizeType.PHYSICAL;
import static org.isite.operation.support.enums.PrizeType.THANK;

/**
 * @Description 奖品发放接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PrizeGiver implements Strategy<PrizeType> {
    private ActivityCache activityCache;
    protected PrizeRecordService prizeRecordService;

    /**
     * 修改奖品记录为已抽奖、发放指定奖品，更新奖品库存信息。
     * 事务传递REQUIRED(默认值):如果当前没有事务,就新建一个事务,如果已经存在一个事务中,加入到这个事务中
     */
    @Synchronized(locks = {
            // 总库存小于等于0时，不做库存校验，不需要加分布式锁
            @Lock(name = LOCK_PRIZE, keys = "#prize", condition = "#prize.totalInventory > 0")
    })
    @Transactional(rollbackFor = Exception.class)
    public void execute(Activity activity, Prize prize, PrizeRecordPo recordPo) {
        prizeRecordService.updateReceiveStatus(recordPo, prize);
        if (TRUE.equals(recordPo.getLockStatus())) {
            activityCache.decrLockInventory(activity, prize);
        } else if (prize.getTotalInventory() > ZERO) {
            isTrue(prize.getTotalInventory() > prize.getConsumeInventory(),
                    getMessage("prize.notInventory", "the prize is gone"));
            activityCache.incrConsumeInventory(activity, prize);
        }
        grantPrize(recordPo);
    }

    /**
     * 调用奖品授权接口
     */
    protected void grantPrize(PrizeRecordPo prizeRecordPo) {
        // 根据奖品类型调用奖品授权接口
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setActivityCache(ActivityCache activityCache) {
        this.activityCache = activityCache;
    }

    @Override
    public PrizeType[] getIdentities() {
        return new PrizeType[] {THANK, PHYSICAL};
    }
}
