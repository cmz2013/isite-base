package org.isite.operation.service;

import org.isite.operation.mapper.InviteRecordMapper;
import org.isite.operation.po.InviteRecordPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.time.LocalDateTime;

import static tk.mybatis.mapper.weekend.Weekend.of;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class InviteRecordService extends TaskRecordService<InviteRecordPo> {

    @Autowired
    public InviteRecordService(InviteRecordMapper mapper) {
        super(mapper);
    }

    public int countInviteRecord(int activityId, int taskId, LocalDateTime startTime, long inviterId) {
        Weekend<InviteRecordPo> weekend = of(InviteRecordPo.class);
        WeekendCriteria<InviteRecordPo, Object> criteria = weekend.weekendCriteria()
                .andEqualTo(InviteRecordPo::getInviterId, inviterId)
                .andEqualTo(InviteRecordPo::getActivityId, activityId)
                .andEqualTo(InviteRecordPo::getTaskId, taskId);
        if (null != startTime) {
            criteria.andGreaterThanOrEqualTo(InviteRecordPo::getFinishTime, startTime);
        }
        return this.count(weekend);
    }
}
