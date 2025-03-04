package org.isite.commons.cloud.utils;

import org.springframework.util.AntPathMatcher;
/**
 * @Description 路径匹配工具.
 * @Author <font color='blue'>zhangcm</font>
 */
public class RequestPathMatcher extends AntPathMatcher {

    public RequestPathMatcher() {
        super();
        //设置路径匹配不区分大小写
        this.setCaseSensitive(Boolean.FALSE);
    }
}
