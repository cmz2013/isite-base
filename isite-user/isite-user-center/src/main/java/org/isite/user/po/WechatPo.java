package org.isite.user.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "wechat")
public class WechatPo extends Po<Long> {

    private Long userId;
    /**
     * 微信公众号用户的唯一标识
     */
    private String openId;
    private String nickname;
}
