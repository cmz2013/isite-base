package org.isite.user.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.BuiltIn;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Description 用户信息PO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "user")
public class UserPo extends Po<Long> implements BuiltIn {
    /**
     * 登录名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 用户状态
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private SwitchStatus status;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 系统内置(1：是，0：否)
     */
    private Boolean internal;
    /**
     * 备注
     */
    private String remark;
}
