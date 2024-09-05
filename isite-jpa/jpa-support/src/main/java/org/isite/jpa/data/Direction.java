package org.isite.jpa.data;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 排序方向枚举类
 * @Author <font color='blue'>zhangcm</font>
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