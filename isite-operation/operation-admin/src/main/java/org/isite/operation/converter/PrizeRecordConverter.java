package org.isite.operation.converter;

import org.isite.operation.data.vo.Prize;
import org.isite.operation.data.vo.Task;
import org.isite.operation.po.InviteRecordPo;
import org.isite.operation.po.PrizePo;
import org.isite.operation.po.PrizeRecordPo;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.isite.misc.data.enums.ObjectType.OPERATION_INVITE_RECORD;

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
     * 1）运营任务异步方式发放奖品，状态为待领取不扣减实物库存，防止用户不领取（先到先得）。
     * 2）活动奖品必须通过活动接口同步发放，避免异步操作时出现并发锁冲突、库存不足等原因无法发放，信息不能同步给用户.
     */
    public static PrizeRecordPo toPrizeRecordPo(
            Task task, InviteRecordPo inviteRecordPo, Prize prize) {
        PrizeRecordPo prizeRecordPo = new PrizeRecordPo();
        prizeRecordPo.setTaskId(task.getId());
        prizeRecordPo.setActivityPid(inviteRecordPo.getActivityPid());
        prizeRecordPo.setObjectType(OPERATION_INVITE_RECORD);
        prizeRecordPo.setObjectValue(valueOf(inviteRecordPo.getId()));
        prizeRecordPo.setFinishTime(new Date(currentTimeMillis()));
        prizeRecordPo.setUserId(inviteRecordPo.getInviterId());
        prizeRecordPo.setActivityId(inviteRecordPo.getActivityId());
        prizeRecordPo.setIdempotentKey(inviteRecordPo.getIdempotentKey());
        //在奖品记录中保存奖品快照信息，但是不锁定奖品（不更新已锁定库存），只能通过管理页面设置抽奖必中更新已锁定库存
        prizeRecordPo.setLockStatus(FALSE);
        prizeRecordPo.setReceiveStatus(FALSE);
        toPrizeRecordPo(prizeRecordPo, prize);
        return prizeRecordPo;
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
