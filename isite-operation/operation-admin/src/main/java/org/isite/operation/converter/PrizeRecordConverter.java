package org.isite.operation.converter;

import org.isite.operation.data.vo.Prize;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.PrizeRecordPo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeRecordConverter {

    private PrizeRecordConverter() {
    }

    /**
     * 在奖品记录中保存奖品快照信息
     */
    public static void toPrizeRecordPo(PrizeRecordPo prizeRecordPo, Prize prize) {
        prizeRecordPo.setPrizeId(prize.getId());
        prizeRecordPo.setThirdPrizeValue(prize.getThirdPrizeValue());
        prizeRecordPo.setPrizeName(prize.getPrizeName());
        prizeRecordPo.setPrizeType(prize.getPrizeType());
        prizeRecordPo.setPrizeImage(prize.getPrizeImage());
    }

    /**
     * 在奖品记录中保存奖品快照信息
     */
    public static void toPrizeRecordPo(PrizeRecordPo prizeRecordPo, PrizePo prizePo) {
        prizeRecordPo.setPrizeId(prizePo.getId());
        prizeRecordPo.setThirdPrizeValue(prizePo.getThirdPrizeValue());
        prizeRecordPo.setPrizeName(prizePo.getPrizeName());
        prizeRecordPo.setPrizeType(prizePo.getPrizeType());
        prizeRecordPo.setPrizeImage(prizePo.getPrizeImage());
    }

    public static PrizeRecordPo toPrizeRecordPo(int prizeId, long userId, Boolean receiveStatus) {
        PrizeRecordPo recordPo = new PrizeRecordPo();
        recordPo.setPrizeId(prizeId);
        recordPo.setUserId(userId);
        recordPo.setReceiveStatus(receiveStatus);
        return recordPo;
    }

    public static PrizeRecordPo toPrizeRecordPo(int activityId, int taskId, long userId) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        prizeRecordPo.setActivityId(activityId);
        prizeRecordPo.setTaskId(taskId);
        prizeRecordPo.setUserId(userId);
        return prizeRecordPo;
    }
}
