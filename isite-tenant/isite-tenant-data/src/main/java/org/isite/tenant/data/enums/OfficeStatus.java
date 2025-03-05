package org.isite.tenant.data.enums;

import org.isite.commons.lang.enums.Enumerable;
/**
 * @Description 员工岗位状态
 * @Author <font color='blue'>zhangcm</font>
 */
public enum OfficeStatus implements Enumerable<Integer> {
    /**
     * 在职
     */
    NORMAL(1),
    /**
     * 离职
     */
    DIMISSION(2);

    private final Integer code;

    OfficeStatus(Integer code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
