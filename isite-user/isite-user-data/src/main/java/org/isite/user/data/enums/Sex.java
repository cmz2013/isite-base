package org.isite.user.data.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public enum Sex implements Enumerable<Integer> {

    UNKNOWN(0),
    MAN(1),
    WOMAN(2);

    private final Integer code;

    Sex(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
