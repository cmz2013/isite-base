package org.isite.user.data.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 用户私密信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserSecret {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 系统内置(1：是，0：否)
     */
    private Boolean internal;
}
