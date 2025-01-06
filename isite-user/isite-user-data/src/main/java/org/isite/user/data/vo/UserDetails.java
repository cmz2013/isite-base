package org.isite.user.data.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserDetails extends UserBasic {
    /**
     * 是否为VIP会员
     */
    private boolean vip;
}
