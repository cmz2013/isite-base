package org.isite.security.login;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.security.data.enums.CodeLoginMode;
import org.isite.security.data.vo.OauthUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @Description 基于用户密码模式实现：短信/邮件验证码登录、微信授权码登录等
 * @Author <font color='blue'>zhangcm</font>
 */
public interface CodeLogin extends Strategy<CodeLoginMode> {
    /**
     * @Description 查询用户信息
     */
    OauthUser getOauthUser(String username, UsernamePasswordAuthenticationToken token);
    /**
     * 验证用户授权码/验证码
     */
    boolean checkCode(String agent, String code);
}
