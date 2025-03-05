package org.isite.security.converter;

import org.isite.commons.lang.Constants;
import org.isite.jpa.data.JpaConstants;
import org.isite.tenant.data.vo.Tenant;

import java.util.HashMap;
import java.util.Map;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TenantConverter {

    private TenantConverter() {
    }

    public static Map<String, Object> toTenantMap(Tenant tenant) {
        Map<String, Object> result = new HashMap<>(Constants.TWO);
        result.put(JpaConstants.FIELD_ID, tenant.getId());
        result.put("tenantName", tenant.getTenantName());
        return result;
    }
}
