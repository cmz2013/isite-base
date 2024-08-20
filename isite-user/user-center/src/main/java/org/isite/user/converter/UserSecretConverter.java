package org.isite.user.converter;

import org.isite.user.po.UserPo;
import org.isite.user.data.vo.UserSecret;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class UserSecretConverter {

    private UserSecretConverter() {
    }

    public static UserSecret toSecret(UserPo userPo) {
        if (null == userPo) {
            return null;
        }
        UserSecret userSecret = new UserSecret();
        userSecret.setUserId(userPo.getId());
        userSecret.setUserName(userPo.getUserName());
        userSecret.setPassword(userPo.getPassword());
        userSecret.setPhone(userPo.getPhone());
        userSecret.setEmail(userPo.getEmail());
        userSecret.setInternal(userPo.getInternal());
        return userSecret;
    }
}
