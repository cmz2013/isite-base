package org.isite.security.converter;

import org.isite.security.data.vo.OauthEmployee;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.isite.commons.lang.data.Constants.THREE;
import static org.isite.security.converter.ResourceConverter.toResourceMaps;
import static org.isite.security.converter.ResourceConverter.toResources;
import static org.isite.security.converter.RoleConverter.toRoleMaps;
import static org.isite.security.converter.TenantConverter.toTenantMap;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class EmployeeConverter {
    private static final String USERNAME = "userName";
    private static final String ROLES = "roles";
    private static final String RESOURCES = "resources";
    private static final String TENANT = "tenant";

    private EmployeeConverter() {
    }

    /**
     * 员工信息序列化
     */
    public static Map<String, Object> toEmployeeMap(OauthEmployee employee, boolean hasRole) {
        Map<String, Object> data = new HashMap<>(THREE);
        data.put(USERNAME, employee.getUsername());
        if (isNotTrue(hasRole)) {
            data.put(RESOURCES, toResourceMaps(toResources(employee.getRoles())));
        } else {
            data.put(ROLES, toRoleMaps(employee.getRoles()));
        }
        data.put(TENANT, toTenantMap(employee.getTenant()));
        return data;
    }
}
