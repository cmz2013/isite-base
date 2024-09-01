package org.isite.operation.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 积分类型
 * @Author <font color='blue'>zhangcm</font>
 */
public enum ScoreType implements Enumerable<Integer> {
    /**
     * 会员积分
     */
    VIP_SCORE(1, "会员积分"),
    /**
     * 活动积分
     */
    ACTIVITY_SCORE(2, "活动积分");

    private final Integer code;
    private final String label;

    ScoreType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public String getLabel() {
        return label;
    }
}
