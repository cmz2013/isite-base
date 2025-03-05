package org.isite.operation.support.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.encoder.NumberEncoder;
import org.isite.operation.support.enums.BusinessIdentity;
import org.isite.operation.support.enums.InviteCodeType;
/**
 * @Description 邀请类型的任务，行为参数类是InviteParam，或者是继承InviteParam的子类
 * @Author <font color='blue'>zhangcm</font>
 */
public class InviteParam {
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
            Assert.notBlank(inviteCode, "inviteCode cannot be null");
            inviterId = NumberEncoder.decode(inviteCode,
                    BusinessIdentity.OPERATION_ACTIVITY.getCode(), InviteCodeType.USER_ID.getCode());
        }
        return inviterId;
    }
}
