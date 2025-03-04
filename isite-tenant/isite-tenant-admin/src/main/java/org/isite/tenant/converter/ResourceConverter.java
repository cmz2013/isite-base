package org.isite.tenant.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.tenant.data.dto.ResourceDto;
import org.isite.tenant.po.ResourcePo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResourceConverter {

    private ResourceConverter() {
    }

    public static ResourcePo toResourcePo(ResourceDto resourceDto) {
        ResourcePo resourcePo = DataConverter.convert(resourceDto, ResourcePo::new);
        if (null == resourcePo.getHref()) {
            resourcePo.setHref(Constants.BLANK_STR);
        }
        if (null == resourcePo.getIcon()) {
            resourcePo.setIcon(Constants.BLANK_STR);
        }
        if (null == resourcePo.getSort()) {
            resourcePo.setSort(Constants.ZERO);
        }
        if (null == resourcePo.getRemark()) {
            resourcePo.setRemark(Constants.BLANK_STR);
        }
        return resourcePo;
    }
}
