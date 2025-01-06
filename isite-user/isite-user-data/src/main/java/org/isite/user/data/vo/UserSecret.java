package org.isite.user.data.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 用户私密信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserSecret extends UserBasic {
    /**
     * 密码
     */
    private String password;
}
