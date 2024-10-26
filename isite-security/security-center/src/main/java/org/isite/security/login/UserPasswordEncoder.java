package org.isite.security.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class UserPasswordEncoder extends BCryptPasswordEncoder {

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(rawPassword, encodedPassword);
    }
}
