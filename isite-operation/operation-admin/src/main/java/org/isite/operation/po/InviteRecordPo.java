package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

/**
 * 活动邀请记录
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "invite_record")
public class InviteRecordPo extends TaskRecordPo {
    /**
     * 邀请人ID
     */
    private Long inviterId;
    /**
     * 被邀请人ID
     */
    private Long visitorId;
}
