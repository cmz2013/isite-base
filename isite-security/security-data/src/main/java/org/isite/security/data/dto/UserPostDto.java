package org.isite.security.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.security.data.enums.VerifyCodeMode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 用户注册信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserPostDto {
    /**
     * 用户名
     */
    @NotBlank
    private String userName;
    /**
     * 真实姓名
     */
    @NotBlank
    private String realName;
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
     * 发送验证码的方式
     */
    @NotNull
    private VerifyCodeMode verifyCodeMode;
    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
