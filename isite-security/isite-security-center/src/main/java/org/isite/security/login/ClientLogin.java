package org.isite.security.login;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.security.data.enums.ClientIdentifier;
import org.isite.security.data.vo.OauthUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @Description 用户登录接口
 * @Author <font color='blue'>zhangcm</font>
 */
public interface ClientLogin extends Strategy<ClientIdentifier> {
    /**
     * @Description 查询用户信息
     */
    OauthUser getOauthUser(String username, UsernamePasswordAuthenticationToken token);
    /**
     * 验证用户密码
     */
    boolean checksPassword(String password, UsernamePasswordAuthenticationToken token);
}
