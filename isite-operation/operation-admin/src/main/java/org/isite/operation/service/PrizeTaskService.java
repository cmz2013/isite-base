package org.isite.operation.service;

import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.data.vo.PrizeReward;
import org.isite.operation.po.PrizeRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.utils.VoUtils.get;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Service
public class PrizeTaskService {
    private PrizeRecordService prizeRecordService;

    /**
     * 保存奖品任务记录
     * @param activity 活动数据
     * @param taskRecord 任务记录
     * @param prizeReward 任务奖励
     */
    public void saveTaskRecord(Activity activity, PrizeRecordPo taskRecord, PrizeReward prizeReward) {
        Prize prize = null;
        if (null != prizeReward) {
            notNull(prizeReward.getPrizeId(), "prizeReward.prizeId must not be null");
            prize = get(activity.getPrizes(), prizeReward.getPrizeId());
            notNull(prize, getMessage("prize.notFound", "prize not found"));
        }
        //在奖品记录中保存奖品快照信息，但是不锁定奖品（不更新已锁定库存），只能通过管理页面设置抽奖必中更新已锁定库存
        taskRecord.setLockStatus(FALSE);
        //活动奖品必须通过活动接口同步发放，防止异步操作时出现并发锁冲突、库存不足等原因无法发放，信息不能同步给用户
        taskRecord.setReceiveStatus(FALSE);
        if (null != prize) {
            taskRecord.setPrizeId(prize.getId());
            taskRecord.setThirdPrizeValue(prize.getThirdPrizeValue());
            taskRecord.setPrizeImage(prize.getPrizeImage());
            taskRecord.setPrizeType(prize.getPrizeType());
            taskRecord.setPrizeName(prize.getPrizeName());
        }
        prizeRecordService.insert(taskRecord);
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }
}
