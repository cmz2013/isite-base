package org.isite.operation.prize;

import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.isite.operation.support.enums.PrizeType;
import org.isite.operation.support.enums.ScoreType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 * @Description VIP积分发放接口（不区分用户是否为会员）。
 * ScoreGiver 可以设置奖品库存，限制发放的总份数。
 * ScoreTaskExecutor 发放积分时，可以限制每人的发放次数，
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class VipScoreGiver extends PrizeGiver {
    private ScoreRecordService scoreRecordService;

    @Override
    protected void grantPrize(PrizeRecordPo prizeRecordPo) {
        ScoreRecordPo scoreRecordPo = new ScoreRecordPo();
        scoreRecordPo.setScoreType(ScoreType.VIP_SCORE);
        scoreRecordPo.setScoreValue(Integer.parseInt(prizeRecordPo.getThirdPrizeValue()));
        scoreRecordPo.setTaskId(prizeRecordPo.getTaskId());
        scoreRecordPo.setActivityPid(prizeRecordPo.getActivityPid());
        scoreRecordPo.setObjectType(prizeRecordPo.getObjectType());
        scoreRecordPo.setObjectValue(prizeRecordPo.getObjectValue());
        scoreRecordPo.setFinishTime(LocalDateTime.now());
        scoreRecordPo.setUserId(prizeRecordPo.getUserId());
        scoreRecordPo.setActivityId(prizeRecordPo.getActivityId());
        scoreRecordPo.setRemark(prizeRecordPo.getRemark());
        scoreRecordPo.setIdempotentKey(prizeRecordPo.getIdempotentKey());
        scoreRecordService.insert(scoreRecordPo);
    }

    @Autowired
    public void setScoreRecordService(ScoreRecordService scoreRecordService) {
        this.scoreRecordService = scoreRecordService;
    }

    @Override
    public PrizeType[] getIdentities() {
        return new PrizeType[]{PrizeType.VIP_SCORE};
    }
}
