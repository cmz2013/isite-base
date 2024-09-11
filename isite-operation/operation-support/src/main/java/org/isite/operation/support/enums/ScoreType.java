package org.isite.operation.support.enums;

import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 积分类型
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public enum ScoreType implements Enumerable<Integer> {
    /**
     * 会员积分，VIP会员通过完成积分任务可以获得会员积分。一个会员积分可以抵扣1分钱，有效期为1年
     */
    VIP_SCORE(1, "会员积分"),
    /**
     * 活动积分。活动积分仅用于该活动奖励，活动结束后失效
     */
    ACTIVITY_SCORE(2, "活动积分");

    private final Integer code;
    private final String label;

    ScoreType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }
}
