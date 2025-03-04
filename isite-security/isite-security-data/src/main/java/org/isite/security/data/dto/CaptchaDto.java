package org.isite.security.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.security.data.enums.CaptchaType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class CaptchaDto {
    /**
     * 验证码
     */
    @NotBlank
    private String code;
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
}
