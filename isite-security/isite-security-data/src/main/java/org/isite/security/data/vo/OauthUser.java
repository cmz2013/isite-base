package org.isite.security.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.isite.jpa.data.BuiltIn;
import org.isite.tenant.data.vo.Tenant;
import org.isite.user.data.enums.Sex;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * @Description 用户认证授权信息
 * @Author <font color='blue'>zhangcm</font>
 */
public class OauthUser implements UserDetails, BuiltIn, Serializable {
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
    private String username;
    /**
     * 头像url
     */
    @Setter
    @Getter
    private String headImg;
    /**
     * 性别
     */
    @Setter
    @Getter
    private Sex sex;
    /**
     * 用户密码
     */
    @Setter
    private String password;
    /**
     * 系统内置用户，不需要设置角色权限，默认拥有所有权限
     */
    private Boolean internal;

    @Setter
    private boolean enabled = TRUE;
    /**
     * 租户ID
     */
    @Setter
    @Getter
    private Tenant tenant;
    /**
     * 员工ID
     */
    @Setter
    @Getter
    private Integer employeeId;
    /**
     * 数据接口权限。key：serviceId, value: Set集合自动去重
     */
    @Setter
    @Getter
    private Map<String, Set<DataAuthority>> authorityMap;

    /**
     * 获取当前服务的数据接口权限
     */
    @Override
    @JsonIgnore
    public Set<DataAuthority> getAuthorities() {
        //stream().flatMap()用于将一个流中的每个元素转换为另一个流，然后将这些流合并成一个单一的流
        return isEmpty(authorityMap) ? null : this.authorityMap.values().stream().flatMap(Set::stream).collect(toSet());
    }

    public Set<DataAuthority> getAuthorities(String serviceId) {
        return isEmpty(authorityMap) ? null : this.authorityMap.get(serviceId);
    }

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
        return enabled;
    }

    @Override
    public String getUsername() {
        return this.username;
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
