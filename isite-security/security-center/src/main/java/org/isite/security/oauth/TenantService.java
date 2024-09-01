package org.isite.security.oauth;

import org.isite.commons.cloud.sign.SignSecret;
import org.isite.security.data.vo.OauthEmployee;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.security.converter.DataAuthorityConverter.toDataAuthority;
import static org.isite.security.web.utils.SecurityUtils.getTokenValue;
import static org.isite.tenant.client.RbacAccessor.getRbac;
import static org.isite.tenant.data.constants.TenantConstants.SERVICE_ID;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class TenantService {

    private SignSecret signSecret;
    private TokenStore tokenStore;

    /**
     * 多租户的用户，登录以后可以切换租户，同时更新rbac权限信息
     */
    public OauthEmployee changeTenant(OauthEmployee employee, Integer tenantId) {
        Rbac rbac = getRbac(new LoginDto(tenantId, employee.getUserId(), employee.getClientId()),
                signSecret.password(SERVICE_ID));

        notNull(rbac, getMessage("Tenant.unavailable", "tenant unavailable"));
        employee.setTenant(rbac.getTenant());
        employee.setEmployeeId(rbac.getEmployeeId());
        employee.setAuthorities(toDataAuthority(rbac.getDataApis()));
        employee.setRoles(rbac.getRoles());

        OAuth2AccessToken accessToken = tokenStore.readAccessToken(getTokenValue());
        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
        authentication.setDetails(employee);
        tokenStore.storeAccessToken(accessToken, authentication);
        return employee;
    }

    @Autowired
    public void setSignSecret(SignSecret signSecret) {
        this.signSecret = signSecret;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }
}
