package org.isite.security.login;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.security.data.enums.ClientIdentifier;

/**
 * @Description 用户登录接口
 * @Author <font color='blue'>zhangcm</font>
 */
public interface LoginHandler extends Strategy<ClientIdentifier> {
    /**
     * 用户登录信息检索
     */
    UserDetailsService getUserDetailsService();
    /**
     * 用户密码校验
     */
    PasswordMatcher getPasswordMatcher();
}
