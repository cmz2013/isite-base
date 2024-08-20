package org.isite.security.data.oauth;

import lombok.Getter;
import lombok.Setter;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.data.vo.Tenant;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * @Description 企业（租户）员工认证授权信息
 * 注意：同一个用户名，企业员工token可以作为用户中心token使用，但反之则不可以，必须在企业端重新登录
 * @Author <font color='blue'>zhangcm</font>
 */
@Setter
public class OauthEmployee extends OauthUser {
    /**
     * 租户ID
     */
    @Getter
    private Tenant tenant;
    /**
     * 员工ID
     */
    @Getter
    private Long employeeId;
    /**
     * 数据接口权限。key：serviceId, value: Set集合自动去重
     */
    private Map<String, Set<DataAuthority>> authorities;
    /**
     * 角色
     */
    @Getter
    private List<Role> roles;

    /**
     * 获取当前服务的数据接口权限
     */
    @Override
    public Set<DataAuthority> getAuthorities() {
        //stream().flatMap()用于将一个流中的每个元素转换为另一个流，然后将这些流合并成一个单一的流
        return isEmpty(authorities) ? null : this.authorities.values().stream().flatMap(Set::stream).collect(toSet());
    }

    public Set<DataAuthority> getAuthorities(String serviceId) {
        return isEmpty(authorities) ? null : this.authorities.get(serviceId);
    }
}
