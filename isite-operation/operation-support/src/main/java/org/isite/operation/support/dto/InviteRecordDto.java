package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class InviteRecordDto extends Dto<Long> {
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 邀请人ID
     */
    private Long inviterId;
}
