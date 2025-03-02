package org.isite.log.data.constants;

import org.isite.commons.cloud.data.constants.UrlConstants;

/**
 * @Description URL常量
 * 1、url常量命名规则约定：API_ + HTTP Method + 资源Path
 * 2、user-center在数据接口权限白名单中可以用 /user/**，所以不需要PUBLIC_和MY_前缀
 * @Author <font color='blue'>zhangcm</font>
 */
public class LogUrls {

    private LogUrls() {
    }

    public static final String URL_LOG = "/log";
    /**
     * 保存日志接口
     */
    public static final String API_POST_LOG = UrlConstants.URL_API + URL_LOG;
}
