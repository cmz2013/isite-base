package org.isite.security.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.user.data.enums.Sex;

import javax.validation.constraints.NotBlank;

/**
 * @Description 用户注册信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserRegistDto {
    /**
     * 用户名
     */
    @NotBlank
    private String username;
    /**
     * 真实姓名
     */
    @NotBlank
    private String realName;
    /**
     * 头像url
     */
    private String headImg;
    /**
     * 性别
     */
    private Sex sex;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    @NotBlank
    private String phone;
    /**
     * 密码
     */
    @NotBlank
    private String password;
    /**
     * 短信验证码
     */
    @NotBlank
    private String captcha;
}
