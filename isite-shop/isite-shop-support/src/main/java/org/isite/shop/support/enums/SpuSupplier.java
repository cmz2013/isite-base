package org.isite.shop.support.enums;

import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum SpuSupplier implements Enumerable<String> {
    TENANT_RESOURCE("tenant-resource", "租户服务功能权限"),
    USER_VIP("user-vip", "用户中心会员权益");

    private final String code;
    @Getter
    private final String label;

    SpuSupplier(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public String getCode() {
        return code;
    }
}
