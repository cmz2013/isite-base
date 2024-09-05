package org.isite.security.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.jpa.data.BuiltIn;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static java.lang.Boolean.TRUE;

/**
 * @Description 用户认证授权信息
 * @Author <font color='blue'>zhangcm</font>
 */
public class OauthUser implements UserDetails, BuiltIn {
    /**
     * 用户ID
     */
    @Setter
    @Getter
    private Long userId;
    /**
     * 登录账号
     */
    @Setter
    private String userName;
    /**
     * 用户密码
     */
    @Setter
    private String password;
    /**
     * 系统内置用户，不需要设置角色权限，默认拥有所有权限
     */
    private Boolean internal;
    /**
     * 客户端ID
     */
    @Setter
    @Getter
    private String clientId;

    @Override
    public boolean isAccountNonExpired() {
        return TRUE;
    }

    /**
     * 基于用户登录失败次数进行时间窗口统计，超过阈值时用户登录失败账号被锁，isAccountNonLocked返回false，
     * 那么在下次登录时，则会抛出LockedException。
     */
    @Override
    public boolean isAccountNonLocked() {
        return TRUE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return TRUE;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Boolean getInternal() {
        return internal;
    }

    @Override
    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
}
