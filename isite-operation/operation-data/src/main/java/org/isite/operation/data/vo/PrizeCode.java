package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeCode extends Vo<Integer> {
    /**
     * 奖品id
     */
    private Integer prizeId;
    /**
     * 编码
     */
    private String code;
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 领取人id
     */
    private Long userId;
}
