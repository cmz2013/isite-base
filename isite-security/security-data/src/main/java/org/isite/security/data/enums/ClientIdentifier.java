package org.isite.security.data.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 客户端ID
 * @Author <font color='blue'>zhangcm</font>
 */
public enum ClientIdentifier implements Enumerable<String> {
    /**
     * 数据集成管理后台-浏览器
     */
    DATA_BROWSER("data.browser");

    private final String code;

    ClientIdentifier(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
