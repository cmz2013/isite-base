package org.isite.operation.converter;

import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.operation.po.InviteRecordPo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class InviteRecordConverter {

    private InviteRecordConverter() {
    }

    public static InviteRecordPo toInviteRecordSelectivePo(int activityId) {
        InviteRecordPo inviteRecordPo = new InviteRecordPo();
        inviteRecordPo.setUserId(TransmittableHeaders.getUserId());
        inviteRecordPo.setActivityId(activityId);
        return inviteRecordPo;
    }
}
