package org.isite.user.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.jpa.data.BuiltIn;
import org.isite.user.data.enums.Sex;
/**
 * @Description 用户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class UserBasic extends Vo<Long> implements BuiltIn {
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 头像url
     */
    private String headImg;
    /**
     * 性别
     */
    private Sex sex;
    /**
     * 用户状态
     */
    private ActiveStatus status;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 系统内置(1：是，0：否)
     */
    private Boolean internal;
}
