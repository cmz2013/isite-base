package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 可用积分 = 总积分 - 已用积分
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class VipScore {

    private Long userId;
    /**
     * 总积分
     */
    private Integer totalScore;
    /**
     * 已用积分
     */
    private Integer usedScore;
}
