package org.isite.operation.task;

import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.converter.PrizeRecordConverter;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.service.PrizeTaskService;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeReward;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 * @Description 奖品任务父接口，在领奖记录表 prize_record 中，保存用户的奖品记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class PrizeTaskExecutor extends TaskExecutor<PrizeRecordPo> {
    private PrizeTaskService prizeTaskService;
    private PrizeRecordService prizeRecordService;

    @Autowired
    public void setPrizeTaskService(PrizeTaskService prizeTaskService) {
        this.prizeTaskService = prizeTaskService;
    }

    /**
     * 1）运营任务异步方式发放奖品，状态为待领取不扣减实物库存，防止用户不领取（先到先得）。
     * 2）活动奖品必须通过活动接口同步发放，避免异步操作时出现并发锁冲突、库存不足等原因无法发放，信息不能同步给用户.
     */
    @Override
    protected PrizeRecordPo createTaskRecord(
            EventDto eventDto, Activity activity, Task task, LocalDateTime periodStartTime, long taskNumber) {
        PrizeRecordPo prizeRecordPo = super.createTaskRecord(eventDto, activity, task, periodStartTime, taskNumber);
        //在奖品记录中保存奖品快照信息，但是不锁定奖品（不更新已锁定库存），只能通过管理页面设置抽奖必中更新已锁定库存
        prizeRecordPo.setLockStatus(Boolean.FALSE);
        prizeRecordPo.setReceiveStatus(Boolean.FALSE);
        return prizeRecordPo;
    }

    @Override
    protected Reward getReward(Activity activity, Task task, EventDto eventDto) {
        return prizeTaskService.getReward(activity.getPrizes(), TypeUtils.cast(task.getProperty()));
    }

    @Override
    public void saveTaskRecord(Activity activity, PrizeRecordPo taskRecord, Reward reward) {
        if (null == reward) {
            return;
        }
        int prizeId = ((PrizeReward) reward).getPrizeId();
        Prize prize = VoUtils.get(activity.getPrizes(), prizeId);
        Assert.notNull(prize, MessageUtils.getMessage("prize.notFound", "prize not found: " + prizeId));
        PrizeRecordConverter.toPrizeRecordPo(taskRecord, prize);
        prizeRecordService.insert(taskRecord);
    }

    @Override
    protected long countTaskRecord(int activityId, int taskId, @Nullable LocalDateTime startTime, long userId) {
        return prizeRecordService.countPrizeRecord(activityId, taskId, startTime, userId);
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {TaskType.QUESTION_PRIZE};
    }
}
