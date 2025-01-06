package org.isite.security.oauth;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_FORWARDED_HOST;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_FORWARDED_PROTO;
import static org.isite.commons.lang.Constants.SLASH;
import static org.isite.commons.lang.Constants.THREE;
import static org.springframework.security.web.util.UrlUtils.isAbsoluteUrl;

/**
 *
 * @Description
 * 1. 通过/oauth/authorize接口请求授权码时，如果没有登录，则保存当前请求信息，
 * 然后转发到登录页面，登录成功以后再重定向到/oauth/authorize
 *
 * 如果是使用网关路由转发请求，须要重定向到网关，如果重定向到认证鉴权中心地址，
 * 则会丢失session信息，/oauth/authorize不能正常获取用户的认证信息
 *
 * 2. 授权码登录成功以后，请求/oauth/logout,会重定向到登录页面/oauth/login/form
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class WebRedirectStrategy extends DefaultRedirectStrategy {

    @Override
    public void sendRedirect(
            HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String host = request.getHeader(X_FORWARDED_HOST);
        if (isNotBlank(host)) {
            String proto = request.getHeader(X_FORWARDED_PROTO) + "://" + host;
            //如果是网关路由转发请求，重定向到网关
            if (isAbsoluteUrl(url)) {
                if (!url.startsWith(proto)) {
                    url = proto + url.substring(url.indexOf(SLASH, url.indexOf("://") + THREE));
                }
            } else {
                url = proto + url;
            }
        }
        super.sendRedirect(request, response, url);
    }
}
