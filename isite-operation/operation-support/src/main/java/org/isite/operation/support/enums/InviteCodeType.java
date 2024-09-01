package org.isite.operation.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 邀请码类型
 * @Author <font color='blue'>zhangcm</font>
 */
public enum InviteCodeType implements Enumerable<Integer> {
    /**
     * 使用用户ID生成邀请码
     */
    USER_ID(1);

    private final Integer code;

    InviteCodeType(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
