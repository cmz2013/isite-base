package org.isite.shop.support.enums;

import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum SourceType implements Enumerable<Integer> {

    SHOP_WEB(1, "商场WEB");

    private final Integer code;
    @Getter
    private final String label;

    SourceType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
