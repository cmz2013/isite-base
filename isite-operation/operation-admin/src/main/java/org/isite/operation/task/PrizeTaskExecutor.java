package org.isite.operation.task;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.data.vo.PrizeReward;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.Task;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.Boolean.FALSE;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.lang.utils.VoUtils.get;
import static org.isite.operation.converter.PrizeRecordConverter.toPrizeRecordPo;
import static org.isite.operation.data.enums.TaskType.QUESTION_ANSWER_ADOPT_PRIZE;
import static org.isite.operation.data.enums.TaskType.QUESTION_PRIZE;

/**
 * @Description 奖品任务父接口，在领奖记录表 prize_record 中，保存用户的奖品记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PrizeTaskExecutor extends TaskExecutor<PrizeRecordPo> {

    private PrizeRecordService prizeRecordService;

    /**
     * 1）运营任务异步方式发放奖品，状态为待领取不扣减实物库存，防止用户不领取（先到先得）。
     * 2）活动奖品必须通过活动接口同步发放，避免异步操作时出现并发锁冲突、库存不足等原因无法发放，信息不能同步给用户.
     */
    @Override
    protected PrizeRecordPo createTaskRecord(
            EventDto eventDto, Activity activity, Task task, Date periodStartTime, long taskNumber) {
        PrizeRecordPo prizeRecordPo = super.createTaskRecord(
                eventDto, activity, task, periodStartTime, taskNumber);
        //在奖品记录中保存奖品快照信息，但是不锁定奖品（不更新已锁定库存），只能通过管理页面设置抽奖必中更新已锁定库存
        prizeRecordPo.setLockStatus(FALSE);
        prizeRecordPo.setReceiveStatus(FALSE);
        return prizeRecordPo;
    }

    @Override
    public void saveTaskRecord(Activity activity, PrizeRecordPo taskRecord, Reward reward) {
        PrizeReward prizeReward = cast(reward);
        notNull(prizeReward.getPrizeId(), "prizeReward.prizeId cannot be null");
        Prize prize = get(activity.getPrizes(), prizeReward.getPrizeId());
        notNull(prize, getMessage("prize.notFound", "prize not found"));
        toPrizeRecordPo(taskRecord, prize);
        prizeRecordService.insert(taskRecord);
    }

    @Override
    protected long countTaskRecord(int activityId, int taskId, @Nullable Date startTime, long userId) {
        return prizeRecordService.countPrizeRecord(activityId, taskId, startTime, userId);
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {QUESTION_PRIZE, QUESTION_ANSWER_ADOPT_PRIZE};
    }
}
