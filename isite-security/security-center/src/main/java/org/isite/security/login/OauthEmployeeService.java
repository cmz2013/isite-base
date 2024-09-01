package org.isite.security.login;

import org.isite.commons.cloud.sign.SignSecret;
import org.isite.security.data.vo.OauthEmployee;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.isite.tenant.data.vo.Role;
import org.isite.tenant.data.vo.Tenant;
import org.isite.user.data.constants.UserConstants;
import org.isite.user.data.vo.UserSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.security.converter.DataAuthorityConverter.toDataAuthority;
import static org.isite.tenant.client.RbacAccessor.getRbac;
import static org.isite.tenant.client.ResourceAccessor.getResources;
import static org.isite.user.client.UserAccessor.getUserSecret;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class OauthEmployeeService extends UserDetailsService {
    private SignSecret signSecret;

    @Override
    public OauthEmployee getOauthUser(String username, String clientId) {
        UserSecret userSecret = getUserSecret(username, signSecret.password(UserConstants.SERVICE_ID));
        if (null == userSecret) {
            return null;
        }

        OauthEmployee employee = new OauthEmployee();
        employee.setUserId(userSecret.getUserId());
        employee.setUserName(userSecret.getUserName());
        employee.setPassword(userSecret.getPassword());
        employee.setInternal(userSecret.getInternal());
        employee.setClientId(clientId);
        /*
         * 1、内置用户具有最高权限，可以访问所有资源
         * 2、非系统内置的员工在登录时，系统获取第一个租户的rbac权限信息
         * 3、多租户的用户，登录以后可以更换租户，同时更新rbac权限信息
         */
        if (TRUE.equals(employee.getInternal())) {
            Tenant tenant = new Tenant();
            tenant.setId(ZERO);
            employee.setTenant(tenant);
            employee.setEmployeeId((long) ZERO);
            Role role = new Role();
            role.setId(ZERO);
            role.setResources(getResources(clientId, signSecret.password(TenantConstants.SERVICE_ID)));
            employee.setRoles(singletonList(role));
        } else {
            Rbac rbac = getRbac(new LoginDto(userSecret.getUserId(), clientId),
                    signSecret.password(TenantConstants.SERVICE_ID));
            if (null == rbac) {
                return null;
            }
            employee.setTenant(rbac.getTenant());
            employee.setEmployeeId(rbac.getEmployeeId());
            employee.setAuthorities(toDataAuthority(rbac.getDataApis()));
            employee.setRoles(rbac.getRoles());
        }
        return employee;
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

}
