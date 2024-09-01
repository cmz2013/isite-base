package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.operation.support.enums.ScoreType;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ScoreRecordDto extends Dto<Long> {
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 积分类型
     */
    private ScoreType scoreType;
}
