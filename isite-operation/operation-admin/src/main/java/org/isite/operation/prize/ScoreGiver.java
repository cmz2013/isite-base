package org.isite.operation.prize;

import org.isite.operation.data.enums.PrizeType;
import org.isite.operation.data.enums.ScoreType;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.po.ScoreRecordPo;
import org.isite.operation.service.ScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.operation.task.IdempotentKey.toValue;

/**
 * @Description 会员积分发放接口
 * VipScoreGiver发放会员积分，可以设置库存，限制发放的总份数；
 * ScoreTaskExecutor发放会员积分时，可以通过任务次数限制每人的发放次数，但是不能限制总份数
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ScoreGiver extends PrizeGiver {
    private ScoreRecordService scoreRecordService;

    @Override
    protected void grantPrize(PrizeRecordPo prizeRecordPo) {
        ScoreRecordPo scoreRecordPo = new ScoreRecordPo();
        scoreRecordPo.setScoreType(ScoreType.VIP_SCORE);
        scoreRecordPo.setScoreValue(parseInt(prizeRecordPo.getThirdPrizeValue()));
        scoreRecordPo.setTaskId(ZERO);
        scoreRecordPo.setActivityPid(prizeRecordPo.getActivityPid());
        scoreRecordPo.setObjectType(prizeRecordPo.getObjectType());
        scoreRecordPo.setObjectValue(prizeRecordPo.getObjectValue());
        scoreRecordPo.setFinishTime(new Date(currentTimeMillis()));
        scoreRecordPo.setUserId(prizeRecordPo.getUserId());
        scoreRecordPo.setActivityId(prizeRecordPo.getActivityId());
        scoreRecordPo.setRemark(prizeRecordPo.getRemark());

        scoreRecordPo.setIdempotentKey(toValue(
                prizeRecordPo.getActivityId(), ZERO, null, prizeRecordPo.getUserId(),
                scoreRecordService.countScoreRecord(prizeRecordPo.getActivityId(), ZERO,
                        null, prizeRecordPo.getUserId())));
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
