package org.isite.user.data.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserGetDto {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
}
