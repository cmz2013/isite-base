package org.isite.user.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;

import javax.validation.constraints.NotBlank;

/**
 * @Description 用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserDto extends Dto<Long> {
    /**
     * 用户名
     */
    @NotBlank(groups = Add.class)
    private String userName;
    /**
     * 真实姓名
     */
    @NotBlank(groups = Add.class)
    private String realName;
    /**
     * 用户状态
     */
    private SwitchStatus status;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 手机号
     */
    @NotBlank(groups = Add.class)
    private String phone;
}
