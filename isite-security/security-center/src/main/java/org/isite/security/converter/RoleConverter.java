package org.isite.security.converter;

import org.isite.tenant.data.vo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.Constants.FIELD_NAME;
import static org.isite.commons.lang.Constants.THREE;
import static org.isite.jpa.data.JpaConstants.FIELD_ID;
import static org.isite.security.converter.ResourceConverter.toResourceMaps;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class RoleConverter {
    private static final String RESOURCES = "resources";

    private RoleConverter() {
    }

    public static List<Map<String, Object>> toRoleMaps(List<Role> roles) {
        return isEmpty(roles) ? emptyList() :
                roles.stream().map(RoleConverter::toRoleMap).collect(toList());
    }

    private static Map<String, Object> toRoleMap(Role role) {
        Map<String, Object> result = new HashMap<>(THREE);
        result.put(FIELD_ID, role.getId());
        result.put(FIELD_NAME, role.getRoleName());
        result.put(RESOURCES, toResourceMaps(role.getResources()));
        return result;
    }
}
