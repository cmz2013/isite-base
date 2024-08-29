package org.isite.security.login;

import org.isite.security.data.vo.OauthUser;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Primary
@Component
public class BCryptMatcher extends BCryptPasswordEncoder implements PasswordMatcher {

    @Override
    public boolean matches(CharSequence rawPassword, OauthUser user) {
        return super.matches(rawPassword, user.getPassword());
    }
}
