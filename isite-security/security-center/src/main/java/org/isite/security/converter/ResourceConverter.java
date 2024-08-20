package org.isite.security.converter;

import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.data.vo.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.lang.data.Constants.FIELD_NAME;
import static org.isite.commons.lang.data.Constants.SEVEN;
import static org.isite.commons.lang.utils.TreeUtils.merge;
import static org.isite.commons.lang.utils.VoUtils.get;
import static org.isite.jpa.data.Constants.FIELD_ID;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResourceConverter {
    private static final String CHILDREN = "children";
    private static final String SORT = "sort";
    private static final String TYPE = "type";
    private static final String HREF = "href";
    private static final String ICON = "icon";

    private ResourceConverter() {
    }

    public static List<Map<String, Object>> toResourceMaps(List<Resource> resources) {
        return isEmpty(resources) ? emptyList() : resources.stream().map(ResourceConverter::toResourceMap).collect(toList());
    }

    private static Map<String, Object> toResourceMap(Resource resource) {
        Map<String, Object> result = new HashMap<>(SEVEN);
        result.put(FIELD_ID, resource.getId());
        result.put(SORT, resource.getSort());
        result.put(TYPE, resource.getType());
        result.put(HREF, resource.getHref());
        result.put(ICON, resource.getIcon());
        result.put(FIELD_NAME, resource.getName());
        if (isNotEmpty(resource.getChildren())) {
            List<Resource> resources = resource.getChildren();
            result.put(CHILDREN, resources.stream().map(ResourceConverter::toResourceMap).collect(toList()));
        }
        return result;
    }

    /**
     * 合并资源树
     */
    public static List<Resource> toResources(List<Role> roles) {
        if (isEmpty(roles)) {
            return emptyList();
        }
        List<Resource> resources = new ArrayList<>();
        roles.forEach(role -> role.getResources().forEach(item -> {
            Resource resource = get(resources, item.getId());
            if (null == resource) {
                resources.add(item);
            } else {
                merge(item, resource);
            }
        }));
        return resources;
    }
}
