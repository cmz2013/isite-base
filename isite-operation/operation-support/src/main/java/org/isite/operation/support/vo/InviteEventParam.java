package org.isite.operation.support.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import static org.isite.commons.lang.Assert.notBlank;
import static org.isite.commons.lang.encoder.NumberEncoder.decode;
import static org.isite.operation.support.enums.BusinessIdentity.OPERATION_ACTIVITY;
import static org.isite.operation.support.enums.InviteCodeType.USER_ID;

/**
 * @Description 邀请类型的任务，行为参数类是InviteEventParam，或者是继承InviteEventParam的子类
 * @Author <font color='blue'>zhangcm</font>
 */
public class InviteEventParam {
    /**
     * 邀请码
     */
    @Getter
    @Setter
    private String inviteCode;

    /**
     * 邀请人ID
     */
    private Long inviterId;

    @JsonIgnore
    public long getInviterId() {
        if (null == inviterId) {
            notBlank(inviteCode, "inviteCode cannot be null");
            inviterId = decode(inviteCode, OPERATION_ACTIVITY.getCode(), USER_ID.getCode());
        }
        return inviterId;
    }
}
