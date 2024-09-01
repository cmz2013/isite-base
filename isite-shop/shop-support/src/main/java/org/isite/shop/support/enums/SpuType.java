package org.isite.shop.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum SpuType implements Enumerable<Integer>  {
    /**
     * 功能权限
     */
    TENANT_RESOURCE(1),
    /**
     * VIP会员
     */
    USER_VIP(2);

    private final Integer code;

    SpuType(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
