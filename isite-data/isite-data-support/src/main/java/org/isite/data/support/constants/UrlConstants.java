package org.isite.data.support.constants;

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_API;

/**
 * @Description URL常量类
 * url常量命名规则约定：API_/MY_/PUBLIC_ + HTTP Method + 资源Path
 * @Author <font color='blue'>zhangcm</font>
 */
public class UrlConstants {

    private UrlConstants() {
    }

    public static final String URL_DATA = "/data";
    /**
     * 调用数据接口
     */
    public static final String API_GET_RPC = URL_API + URL_DATA + "/rpc/{wsType}/{apiId}";
    /**
     * 调用执行器接口实现数据补偿
     */
    public static final String PUT_LOG_RETRY = "/log/retry";
}