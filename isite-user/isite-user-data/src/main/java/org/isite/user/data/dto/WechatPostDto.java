package org.isite.user.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Description 微信用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class WechatPostDto extends UserPostDto {

    @NotBlank
    private String openId;
}
