package org.isite.security.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.security.data.enums.CaptchaType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 用户修改密码的DTO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserSecretDto {
    /**
     * 用户登录名
     */
    @NotBlank
    private String username;
    /**
     * 用户密码
     */
    @NotBlank
    private String password;
    /**
     * 发送验证码的方式
     */
    @NotNull
    private CaptchaType captchaType;
    /**
     * 密保终端：email或手机号等
     */
    @NotBlank
    private String agent;
    /**
     * 验证码
     */
    @NotBlank
    private String captcha;
}
