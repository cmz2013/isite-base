package org.isite.operation.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 邀请人业务身份
 * @Author <font color='blue'>zhangcm</font>
 */
public enum BusinessIdentity implements Enumerable<Integer> {
    /**
     * 邀请好友参加运营活动
     */
    OPERATION_ACTIVITY(1);

    private final Integer code;

    BusinessIdentity(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
