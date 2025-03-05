package org.isite.commons.lang.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @Description : 启用状态
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonDeserialize(using = ActiveStatusDeserializer.class)
public enum ActiveStatus implements Enumerable<Integer> {
    /**
     * 禁用的
     */
    DISABLED(0),
    /**
     * 启用的
     */
    ENABLED(1);

    private final Integer code;

    ActiveStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
