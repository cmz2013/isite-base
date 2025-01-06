package org.isite.data.support.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 接口类型
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonDeserialize(using = WsTypeDeserializer.class)
public enum WsType implements Enumerable<Integer> {
    /**
     * 远程回调接口
     */
    CALLBACK(0),
    /**
     * 本地服务接口
     */
    SERVICE(1);

    WsType(Integer code) {
        this.code = code;
    }

    private final Integer code;

    @Override
    public Integer getCode() {
        return code;
    }
}
