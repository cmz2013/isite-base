package org.isite.security.login;

import org.isite.security.data.oauth.OauthUser;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public interface PasswordMatcher {

    boolean matches(CharSequence rawPassword, OauthUser user);
}
