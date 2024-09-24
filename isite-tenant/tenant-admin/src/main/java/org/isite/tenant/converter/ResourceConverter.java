package org.isite.tenant.converter;

import org.isite.tenant.data.dto.ResourceDto;
import org.isite.tenant.po.ResourcePo;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResourceConverter {

    private ResourceConverter() {
    }

    public static ResourcePo toResourcePo(ResourceDto resourceDto) {
        ResourcePo resourcePo = convert(resourceDto, ResourcePo::new);
        if (null == resourcePo.getHref()) {
            resourcePo.setHref(BLANK_STRING);
        }
        if (null == resourcePo.getIcon()) {
            resourcePo.setIcon(BLANK_STRING);
        }
        if (null == resourcePo.getSort()) {
            resourcePo.setSort(ZERO);
        }
        if (null == resourcePo.getRemark()) {
            resourcePo.setRemark(BLANK_STRING);
        }
        return resourcePo;
    }
}
