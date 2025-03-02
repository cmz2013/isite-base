package org.isite.security.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.utils.TreeUtils;
import org.isite.commons.cloud.utils.VoUtils;
import org.isite.commons.lang.Constants;
import org.isite.jpa.data.JpaConstants;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResourceConverter {
    private static final String CHILDREN = "children";
    private static final String SORT = "sort";
    private static final String TYPE = "type";
    private static final String HREF = "href";
    private static final String ICON = "icon";
    private static final String RESOURCE_NAME = "resourceName";

    private ResourceConverter() {
    }

    public static List<Map<String, Object>> toResourceMaps(List<Resource> resources) {
        return CollectionUtils.isEmpty(resources) ? Collections.emptyList() :
                resources.stream().map(ResourceConverter::toResourceMap).collect(Collectors.toList());
    }

    private static Map<String, Object> toResourceMap(Resource resource) {
        Map<String, Object> result = new HashMap<>(Constants.SEVEN);
        result.put(JpaConstants.FIELD_ID, resource.getId());
        result.put(SORT, resource.getSort());
        result.put(TYPE, resource.getType());
        result.put(HREF, resource.getHref());
        result.put(ICON, resource.getIcon());
        result.put(RESOURCE_NAME, resource.getResourceName());
        if (CollectionUtils.isNotEmpty(resource.getChildren())) {
            List<Resource> resources = resource.getChildren();
            result.put(CHILDREN, resources.stream().map(ResourceConverter::toResourceMap).collect(Collectors.toList()));
        }
        return result;
    }

    /**
     * 合并资源树
     */
    public static List<Resource> toResources(List<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        List<Resource> resources = new ArrayList<>();
        roles.forEach(role -> role.getResources().forEach(item -> {
            Resource resource = VoUtils.get(resources, item.getId());
            if (null == resource) {
                resources.add(item);
            } else {
                TreeUtils.merge(item, resource);
            }
        }));
        return resources;
    }
}
