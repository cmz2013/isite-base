package org.isite.operation.converter;

import org.isite.operation.po.InviteRecordPo;

import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class InviteRecordConverter {

    private InviteRecordConverter() {
    }

    public static InviteRecordPo toInviteRecordSelectivePo(int activityId) {
        InviteRecordPo inviteRecordPo = new InviteRecordPo();
        inviteRecordPo.setUserId(getUserId());
        inviteRecordPo.setActivityId(activityId);
        return inviteRecordPo;
    }
}
