package org.isite.security.converter;

import org.isite.tenant.data.vo.Tenant;

import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.lang.Constants.TWO;
import static org.isite.jpa.data.JpaConstants.FIELD_ID;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TenantConverter {

    private static final String TENANT_NAME = "tenantName";

    private TenantConverter() {
    }

    public static Map<String, Object> toTenantMap(Tenant tenant) {
        Map<String, Object> result = new HashMap<>(TWO);
        result.put(FIELD_ID, tenant.getId());
        result.put(TENANT_NAME, tenant.getTenantName());
        return result;
    }
}
