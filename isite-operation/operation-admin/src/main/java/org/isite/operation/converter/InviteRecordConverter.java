package org.isite.operation.converter;

import org.isite.operation.po.InviteRecordPo;

import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;

/**
 * @author <font color='blue'>zhangcm</font>
 */
public class InviteRecordConverter {

    private InviteRecordConverter() {
    }

    public static InviteRecordPo toInviteRecordPo(int activityId) {
        InviteRecordPo inviteRecordPo = new InviteRecordPo();
        inviteRecordPo.setUserId(getUserId());
        inviteRecordPo.setActivityId(activityId);
        return inviteRecordPo;
    }
}
