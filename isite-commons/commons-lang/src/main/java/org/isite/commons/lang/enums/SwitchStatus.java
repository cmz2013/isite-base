package org.isite.commons.lang.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 开关状态枚举类
 *
 * @author <font color='blue'>zhangcm</font>
 */
@JsonDeserialize(using = SwitchDeserializer.class)
public enum SwitchStatus implements Enumerable<Integer> {
    /**
     * 禁用的
     */
    DISABLED(0),
    /**
     * 启用的
     */
    ENABLED(1);

    private final Integer code;

    SwitchStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
