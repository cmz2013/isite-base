package org.isite.operation.task;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.operation.converter.PrizeRecordConverter;
import org.isite.operation.po.InviteRecordPo;
import org.isite.operation.service.InviteRecordService;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.service.PrizeTaskService;
import org.isite.operation.service.TaskRecordService;
import org.isite.operation.support.dto.EventDto;
import org.isite.operation.support.enums.TaskType;
import org.isite.operation.support.vo.Activity;
import org.isite.operation.support.vo.InviteParam;
import org.isite.operation.support.vo.Prize;
import org.isite.operation.support.vo.PrizeReward;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 * @Description 积分任务父接口。使用活动积分可以兑换奖品
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class InviteTaskExecutor extends TaskExecutor<InviteRecordPo> {
    private PrizeRecordService prizeRecordService;
    private InviteRecordService inviteRecordService;
    private PrizeTaskService prizeTaskService;

    @Override
    protected InviteRecordPo createTaskRecord(
            EventDto eventDto, Activity activity, Task task, LocalDateTime periodStartTime, long taskNumber) {
        InviteRecordPo inviteRecordPo = super.createTaskRecord(eventDto, activity, task, periodStartTime, taskNumber);
        InviteParam inviteParam = TypeUtils.cast(eventDto.getEventParam());
        inviteRecordPo.setInviterId(inviteParam.getInviterId());
        return inviteRecordPo;
    }

    @Override
    protected long getTaskNumber(int activityId, int taskId, LocalDateTime periodStartTime, Integer limit, EventDto eventDto) {
        //已参与活动不能被邀请
        ApplicationContextUtils.getBeans(TaskRecordService.class).values().forEach(taskRecordService -> Assert.isFalse(
                taskRecordService.exists(activityId, eventDto.getUserId()), "can't invite users who already exist"));
        //邀请人任务周期约束限制
        InviteParam inviteParam = TypeUtils.cast(eventDto.getEventParam());
        return getTaskNumber(activityId, taskId, periodStartTime, limit, inviteParam.getInviterId());
    }

    /**
     * 统计邀请人的任务记录条数
     * @param activityId 活动ID
     * @param taskId 任务ID
     * @param startTime 当前任务周期开始时间
     * @param inviterId 邀请人ID
     */
    @Override
    protected long countTaskRecord(int activityId, int taskId, @Nullable LocalDateTime startTime, long inviterId) {
        return inviteRecordService.countInviteRecord(activityId, taskId, startTime, inviterId);
    }

    @Override
    protected Reward getReward(Activity activity, Task task, EventDto eventDto) {
        return prizeTaskService.getReward(activity.getPrizes(), TypeUtils.cast(task.getProperty()));
    }

    @Override
    protected void saveTaskRecord(Activity activity, InviteRecordPo inviteRecordPo, Reward reward) {
        inviteRecordService.insert(inviteRecordPo);
        if (null == reward) {
            return;
        }
        int prizeId = ((PrizeReward) reward).getPrizeId();
        Prize prize = VoUtils.get(activity.getPrizes(), prizeId);
        Assert.notNull(prize, MessageUtils.getMessage("prize.notFound", "prize not found: " + prizeId));
        prizeRecordService.insert(PrizeRecordConverter.toPrizeRecordPo(
                VoUtils.get(activity.getTasks(), inviteRecordPo.getTaskId()), inviteRecordPo, prize));
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setInviteRecordService(InviteRecordService inviteRecordService) {
        this.inviteRecordService = inviteRecordService;
    }

    @Override
    public TaskType[] getIdentities() {
        return new TaskType[] {TaskType.OPERATION_WEBPAGE_INVITE, TaskType.QUESTION_REPLY_INVITE};
    }
}
