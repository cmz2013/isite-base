package org.isite.commons.cloud.data.enums;

import lombok.Getter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.lang.enums.Enumerable;
/**
 * @Description 用户终端类型
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public enum TerminalType implements Enumerable<Integer> {
    /**
     * web浏览器
     */
    WEB(0, UrlConstants.URL_HTML_PC_PATTERN),
    /**
     * 移动应用
     */
    APP(1, UrlConstants.URL_HTML_APP_PATTERN);

    private final Integer code;
    /**
     * 视图匹配模式
     */
    private final String viewPattern;

    TerminalType(Integer code, String viewPattern) {
        this.code = code;
        this.viewPattern = viewPattern;
    }
}