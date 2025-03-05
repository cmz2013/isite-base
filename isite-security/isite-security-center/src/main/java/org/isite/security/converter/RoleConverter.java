package org.isite.security.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Constants;
import org.isite.jpa.data.JpaConstants;
import org.isite.tenant.data.vo.Role;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class RoleConverter {
    private static final String RESOURCES = "resources";
    private static final String ROLE_NAME = "roleName";

    private RoleConverter() {
    }

    public static List<Map<String, Object>> toRoleMaps(List<Role> roles) {
        return CollectionUtils.isEmpty(roles) ? Collections.emptyList() :
                roles.stream().map(RoleConverter::toRoleMap).collect(Collectors.toList());
    }

    private static Map<String, Object> toRoleMap(Role role) {
        Map<String, Object> result = new HashMap<>(Constants.THREE);
        result.put(JpaConstants.FIELD_ID, role.getId());
        result.put(ROLE_NAME, role.getRoleName());
        result.put(RESOURCES, ResourceConverter.toResourceMaps(role.getResources()));
        return result;
    }
}
