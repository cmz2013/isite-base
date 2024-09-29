package org.isite.user.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;

import javax.validation.constraints.NotBlank;

/**
 * @Description 用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserPutDto extends Dto<Long> {
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
}
