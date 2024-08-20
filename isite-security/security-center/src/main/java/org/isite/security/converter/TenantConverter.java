package org.isite.security.converter;

import org.isite.tenant.data.vo.Tenant;

import java.util.HashMap;
import java.util.Map;

import static org.isite.commons.lang.data.Constants.FIELD_NAME;
import static org.isite.commons.lang.data.Constants.TWO;
import static org.isite.jpa.data.Constants.FIELD_ID;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TenantConverter {

    private TenantConverter() {
    }

    public static Map<String, Object> toTenantMap(Tenant tenant) {
        Map<String, Object> result = new HashMap<>(TWO);
        result.put(FIELD_ID, tenant.getId());
        result.put(FIELD_NAME, tenant.getName());
        return result;
    }
}
