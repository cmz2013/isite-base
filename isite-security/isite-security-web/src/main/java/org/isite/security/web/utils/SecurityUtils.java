package org.isite.security.web.utils;

import org.isite.commons.lang.Constants;
import org.isite.security.data.vo.OauthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Base64;
/**
 * @Description 资源服务器认证鉴权工具类
 * @Author <font color='blue'>zhangcm</font>
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前请求的access_token
     */
    public static String getTokenValue() {
        // 通过ThreadLocal的应用取到当前的SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();
        if (null == context) {
            return null;
        }
        Authentication authentication = context.getAuthentication();
        if (null == authentication) {
            return null;
        }
        Object details = authentication.getDetails();
        return details instanceof OAuth2AuthenticationDetails ?
                ((OAuth2AuthenticationDetails) details).getTokenValue() :
                null;
    }

    /**
     * @Description 获取当前登录的用户
     * 注意：如果只查询当前登录用户数据，接口路径约定/my/**，数据接口授权拦截器自动放行
     */
    public static OauthUser getOauthUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (null == context) {
            return null;
        }
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof OauthUser) {
            return (OauthUser) principal;
        }
        return null;
    }

    /**
     * 生成终端 HTTP Basic 认证字符串
     */
    public static String getBasicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + Constants.COLON + password).getBytes());
    }
}
