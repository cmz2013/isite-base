package org.isite.operation.task;

import org.isite.operation.data.dto.EventDto;
import org.isite.operation.data.enums.TaskType;
import org.isite.operation.data.vo.Activity;
import org.isite.operation.data.vo.InviteEventParam;
import org.isite.operation.data.vo.InviteReward;
import org.isite.operation.data.vo.Prize;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.Task;
import org.isite.operation.po.InviteRecordPo;
import org.isite.operation.service.InviteRecordService;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.service.TaskRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBeans;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.lang.utils.VoUtils.get;
import static org.isite.operation.converter.PrizeRecordConverter.toPrizeRecordPo;
import static org.isite.operation.data.enums.TaskType.OPERATION_WEBPAGE_INVITE;
import static org.isite.operation.data.enums.TaskType.QUESTION_ANSWER_INVITE;
import static org.isite.operation.task.IdempotentKey.toValue;

/**
 * @Description 积分任务父接口。使用活动积分可以兑换奖品
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class InviteTaskExecutor extends TaskExecutor<InviteRecordPo> {

    private PrizeRecordService prizeRecordService;
    private InviteRecordService inviteRecordService;

    @Override
    protected InviteRecordPo createTaskRecord(
            EventDto eventDto, Activity activity, Task task, Date periodStartTime, long taskNumber) {
        InviteEventParam inviteEventParam = cast(eventDto.getEventParam());
        long inviterId = inviteEventParam.getInviterId();
        InviteRecordPo inviteRecordPo = new InviteRecordPo();
        inviteRecordPo.setInviterId(inviterId);
        inviteRecordPo.setActivityId(activity.getId());
        inviteRecordPo.setTaskId(task.getId());
        inviteRecordPo.setActivityPid(activity.getPid());
        inviteRecordPo.setObjectType(eventDto.getEventType().getObjectType());
        inviteRecordPo.setObjectValue(eventDto.getObjectValue());
        inviteRecordPo.setFinishTime(new Date(currentTimeMillis()));
        inviteRecordPo.setUserId(eventDto.getUserId());
        inviteRecordPo.setRemark(task.getTaskType().getLabel());
        inviteRecordPo.setIdempotentKey(toValue(activity.getId(), task.getId(), periodStartTime, inviterId, taskNumber));

        return inviteRecordPo;
    }

    @Override
    protected long getTaskNumber(int activityId, int taskId, Date periodStartTime, Integer limit, EventDto eventDto) {
        //已参与活动不能被邀请
        getBeans(TaskRecordService.class).values().forEach(taskRecordService -> isFalse(
                taskRecordService.exists(activityId, eventDto.getUserId()), "can't invite users who already exist"));

        //邀请人任务周期约束限制
        InviteEventParam inviteEventParam = cast(eventDto.getEventParam());
        return getTaskNumber(activityId, taskId, periodStartTime, limit, inviteEventParam.getInviterId());
    }

    /**
     * 统计邀请人的任务记录条数
     * @param activityId 活动ID
     * @param taskId 任务ID
     * @param startTime 当前任务周期开始时间
     * @param inviterId 邀请人ID
     */
    @Override
    protected long countTaskRecord(int activityId, int taskId, @Nullable Date startTime, long inviterId) {
        return inviteRecordService.countInviteRecord(activityId, taskId, startTime, inviterId);
    }

    @Override
    protected void saveTaskRecord(Activity activity, InviteRecordPo inviteRecordPo, Reward reward) {
        inviteRecordService.insert(inviteRecordPo);
        InviteReward inviteReward = cast(reward);
        if (null == inviteReward || null == inviteReward.getPrizeId()) {
            return;
        }
        Prize prize = get(activity.getPrizes(), inviteReward.getPrizeId());
        notNull(prize, getMessage("prize.notFound", "prize not found"));
        prizeRecordService.insert(toPrizeRecordPo(
                get(activity.getTasks(), inviteRecordPo.getTaskId()), inviteRecordPo, prize));
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
        return new TaskType[] {OPERATION_WEBPAGE_INVITE, QUESTION_ANSWER_INVITE};
    }
}
