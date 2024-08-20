package org.isite.security.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.security.data.enums.VerifyCodeMode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class VerifyCodeGetDto {
    /**
     * 发送验证码的方式
     */
    @NotNull
    private VerifyCodeMode verifyCodeMode;
    /**
     * 密保终端：email或手机号等
     */
    @NotBlank
    private String agent;
}
