package org.isite.operation.support.constants;

import static org.isite.commons.cloud.constants.UrlConstants.URL_MY;

/**
 * @Description URL常量
 * url常量命名规则约定：API_/MY_/PUBLIC_ + HTTP Method + 资源Path
 * @Author <font color='blue'>zhangcm</font>
 */
public class UrlConstants {

    private UrlConstants() {
    }

    public static final String URL_OPERATION = "/operation";
    /**
     * 抵扣VIP积分
     */
    public static final String PUT_VIP_SCORE = URL_MY + URL_OPERATION + "/vip/score";
}
