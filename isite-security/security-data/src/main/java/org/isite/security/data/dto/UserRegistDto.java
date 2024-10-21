package org.isite.security.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.security.data.enums.CodeMode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
     * 密码
     */
    @NotBlank
    private String password;
    /**
     * 发送验证码的方式
     */
    @NotNull
    private CodeMode codeMode;
    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
