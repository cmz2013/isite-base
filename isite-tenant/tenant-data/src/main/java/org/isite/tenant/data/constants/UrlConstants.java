package org.isite.tenant.data.constants;

import static org.isite.commons.cloud.constants.UrlConstants.URL_API;

/**
 * @Description URL常量
 * url常量命名规则约定：API_/MY_/PUBLIC_ + HTTP Method + 资源Path
 * @Author <font color='blue'>zhangcm</font>
 */
public class UrlConstants {

    private UrlConstants() {
    }

    public static final String URL_TENANT = "/tenant";
    /**
     * 获取员工信息
     */
    public static final String GET_EMPLOYEE = URL_TENANT + "/employee/{id}";
    /**
     * 更新租户状态
     */
    public static final String PUT_TENANT_STATUS = URL_TENANT + "/{id}/status/{status}";
    /**
     * 员工授权信息
     */
    public static final String API_GET_EMPLOYEE_RBAC = URL_API + URL_TENANT + "/employee/rbac";
    /**
     * 内置用户登录时获取客户端所有资源
     */
    public static final String API_GET_CLIENT_RESOURCES = URL_API + URL_TENANT + "/resources/{clientId}";
    /**
     * 租户查询可以访问的客户端
     */
    public static final String GET_OAUTH_CLIENTS = URL_TENANT + "/oauth/clients";
}
