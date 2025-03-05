package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeRecordDto extends Dto<Long> {
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 领奖状态：true 已领取
     */
    private Boolean receiveStatus;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 必中的（锁定的）领奖记录
     */
    private Boolean lockStatus;
}
