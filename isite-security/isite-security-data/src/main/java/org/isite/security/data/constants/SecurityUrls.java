package org.isite.security.data.constants;

import org.isite.commons.cloud.data.constants.UrlConstants;
/**
 * @Description
 * 1) 自定义API使用oauth（认证鉴权中心）打头，和框架（spring-cloud-starter-oauth2）内置的URL前缀保持一致，支持网关转发请求
 * 2) url常量命名规则约定：API_ + HTTP Method + 资源Path
 * 3）security-center在数据接口权限白名单中可以用 /oauth/**，所以不需要PUBLIC_和MY_前缀
 * @Author <font color='blue'>zhangcm</font>
 */
public class SecurityUrls {

    private SecurityUrls() {
    }
    /**
     * SpringCloud OAuth2框架提供的API：
     * /oauth/authorize 授权码
     * /oauth/token 令牌
     * /oauth/confirm_access 用户批准授权
     */
    public static final String GET_OAUTH_AUTHORIZE = "/oauth/authorize";
    /**
     * URL前缀
     */
    public static final String URL_OAUTH = "/oauth";
    /**
     * 授权码模式用户登录页面
     */
    public static final String URL_LOGIN_FORM = URL_OAUTH + "/login/form";

    public static final String POST_LOGIN_PROCESS = URL_OAUTH + "/login/process";
    /**
     * 授权码模式注销用户登录，返回登录页面
     */
    public static final String URL_OAUTH_LOGOUT = URL_OAUTH + "/logout";
    /**
     * 给资源服务器提供Token认证的API
     */
    public static final String GET_OAUTH_PRINCIPAL = URL_OAUTH + "/principal";
    /**
     * 获取当前登录的用户信息
     */
    public static final String GET_OAUTH_USER = URL_OAUTH + "/user";
    /**
     * 注销当前用户在当前终端的token
     */
    public static final String DELETE_OAUTH_USER = URL_OAUTH + "/user";
    /**
     * 注册用户信息
     */
    public static final String API_POST_USER = UrlConstants.URL_API + URL_OAUTH + "/user";
    /**
     * 更新用户密码
     */
    public static final String API_PUT_USER_PASSWORD = UrlConstants.URL_API + URL_OAUTH + "/user/password";
    /**
     * 获取验证码（不需要登录）
     */
    public static final String API_GET_CAPTCHA = UrlConstants.URL_API + URL_OAUTH + "/captcha";
    /**
     * 校验验证码
     */
    public static final String DELETE_CAPTCHA = URL_OAUTH + "/captcha";
    /**
     * 授权码模式，用户登录以后批准授权
     * 用于替换框架（spring-cloud-starter-oauth2）内置的接口路径 /oauth/confirm_access
     */
    public static final String URL_OAUTH_APPROVAL = URL_OAUTH + "/approval";
    /**
     * 更换租户
     */
    public static final String PUT_OAUTH_TENANT = URL_OAUTH + "/tenant/{tenantId}";
    /**
     * 查询客户端
     */
    public static final String GET_OAUTH_CLIENTS = URL_OAUTH + "/clients";
}
