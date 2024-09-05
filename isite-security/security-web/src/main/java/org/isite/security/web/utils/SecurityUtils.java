package org.isite.security.web.utils;

import org.isite.security.data.vo.OauthEmployee;
import org.isite.security.data.vo.OauthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import static java.util.Base64.getEncoder;
import static org.isite.commons.lang.Constants.COLON;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

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
        SecurityContext context = getContext();
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
        SecurityContext context = getContext();
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
     * @Description 获取当前登录的企业员工
     * 注意：如果只查询当前登录用户数据，接口路径约定/my/**，数据接口授权拦截器自动放行
     */
    public static OauthEmployee getOauthEmployee() {
        OauthUser oauthUser = getOauthUser();
        if (oauthUser instanceof OauthEmployee) {
            return (OauthEmployee) oauthUser;
        }
        return null;
    }

    /**
     * 生成终端 HTTP Basic 认证字符串
     */
    public static String getBasicAuth(String username, String password) {
        return "Basic " + getEncoder().encodeToString((username + COLON + password).getBytes());
    }
}
