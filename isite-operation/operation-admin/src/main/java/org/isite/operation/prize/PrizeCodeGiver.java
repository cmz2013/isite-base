package org.isite.operation.prize;

import org.isite.operation.support.enums.PrizeType;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.isite.operation.support.enums.PrizeType.PRIZE_CODE;

/**
 * @Description 兑奖码发放接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PrizeCodeGiver extends PrizeGiver {

    private PrizeCodeService prizeCodeService;

    /**
     * 发放兑奖码
     */
    @Override
    protected void grantPrize(PrizeRecordPo prizeRecordPo) {
        String prizeCode = prizeCodeService.grantPrizeCode(prizeRecordPo.getUserId(), prizeRecordPo.getPrizeId());
        prizeRecordService.updateThirdPrizeValue(prizeRecordPo.getId(), prizeCode);
    }

    @Autowired
    public void setRedEnvelopeRecordService(PrizeCodeService prizeCodeService) {
        this.prizeCodeService = prizeCodeService;
    }

    @Override
    public PrizeType[] getIdentities() {
        return new PrizeType[] { PRIZE_CODE };
    }
}
