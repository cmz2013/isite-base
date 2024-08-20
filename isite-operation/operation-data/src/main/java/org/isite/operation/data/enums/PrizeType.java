package org.isite.operation.data.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 运营活动奖品类型
 * @Author <font color='blue'>zhangcm</font>
 */
public enum PrizeType implements Enumerable<Integer> {
    /**
     * 感谢参与
     */
    THANK(0),
    /**
     * 实物
     */
    PHYSICAL(1),
    /**
     * 会员积分（可用于抵消支付金额）
     */
    VIP_SCORE(2),
    /**
     * 兑奖码
     */
    PRIZE_CODE(3),
    /**
     * 用户标签
     */
    USER_TAG(4);

    private final Integer code;

    PrizeType(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
