package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;

/**
 * @Description 邀请记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "invite_record")
public class InviteRecordPo extends TaskRecordPo {
    /**
     * @Description inviterId 邀请人ID（解析邀请码获取）; userId 被邀请人ID
     */
    private Long inviterId;

}
