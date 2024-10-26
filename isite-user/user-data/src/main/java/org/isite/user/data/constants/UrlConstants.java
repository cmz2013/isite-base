package org.isite.user.data.constants;

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_API;

/**
 * @Description URL常量
 * 1、url常量命名规则约定：API_ + HTTP Method + 资源Path
 * 2、user-center在数据接口权限白名单中可以用 /user/**，所以不需要PUBLIC_和MY_前缀
 * @Author <font color='blue'>zhangcm</font>
 */
public class UrlConstants {

    private UrlConstants() {
    }

    public static final String URL_USER = "/user";
    /**
     * 根据唯一标识（id、username、phone）获取用户信息
     */
    public static final String GET_USER_DETAILS = URL_USER + "/{identifier}/details";
    /**
     * 分页查询用户列表
     */
    public static final String GET_USERS = URL_USER + "/list";
    /**
     * 根据唯一标识（username、phone、email）查询用户秘钥
     */
    public static final String API_GET_USER_SECRET = URL_API + URL_USER + "/{identifier}/secret";
    /**
     * 注册微信用户信息
     */
    public static final String API_POST_USER_WECHAT = URL_API + URL_USER + "/wechat";
    /**
     * 用户鉴权中心注册用户信息
     */
    public static final String API_POST_USER = URL_API + URL_USER;
    /**
     * 用户鉴权中心更新/设置用户密码
     */
    public static final String API_PUT_USER_PASSWORD = URL_API + URL_USER + "/{userId}/password";
    /**
     *
     */
    public static final String POST_USER_PHONE_IF_ABSENT = URL_USER + "/phone/{phone}";
}
