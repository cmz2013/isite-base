package org.isite.tenant.data.enums;

import org.isite.commons.lang.enums.Enumerable;
/**
 * @Description 系统资源类型枚举
 * @Author <font color='blue'>zhangcm</font>
 */
public enum ResourceType implements Enumerable<String> {
    /**
     * 按钮
     */
    BTN("btn"),
    /**
     * 文件夹
     */
    FOLDER("folder"),
    /**
     * 页面
     */
    PAGE("page");

    private final String code;

    ResourceType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
