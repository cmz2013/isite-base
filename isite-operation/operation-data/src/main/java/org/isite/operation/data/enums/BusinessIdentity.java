package org.isite.operation.data.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * 邀请人业务身份
 * @author <font color='blue'>zhangcm</font>
 */
public enum BusinessIdentity implements Enumerable<Integer> {
    /**
     * 邀请好友参加运营活动
     */
    ACTIVITY(1);

    private final Integer code;

    BusinessIdentity(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
