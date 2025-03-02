package org.isite.operation.support.constants;

import org.isite.commons.cloud.data.constants.UrlConstants;

/**
 * @Description URL常量
 * url常量命名规则约定：API_/MY_/PUBLIC_ + HTTP Method + 资源Path
 * @Author <font color='blue'>zhangcm</font>
 */
public class OperationUrls {

    private OperationUrls() {
    }

    public static final String URL_OPERATION = "/operation";
    /**
     * 使用VIP积分
     */
    public static final String PUT_USE_VIP_SCORE = UrlConstants.URL_MY + URL_OPERATION + "/useScore";
}
