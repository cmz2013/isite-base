package org.isite.commons.lang.data;

import org.isite.commons.lang.enums.Enumerable;

/**
 * 排序方向枚举类
 * @author <font color='blue'>zhangcm</font>
 */
public enum Direction implements Enumerable<String> {
    /**
     * 升序
     */
    ASC,
    /**
     * 降序
     */
    DESC;

    @Override
    public String getCode() {
        return this.name();
    }
}