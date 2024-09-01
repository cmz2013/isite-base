package org.isite.tenant.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.tenant.data.vo.Resource;

import java.util.List;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.lang.utils.ResultUtils.getData;
import static org.isite.tenant.data.constants.TenantConstants.SERVICE_ID;

/**
 * @Description ResourceClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResourceAccessor {

    private ResourceAccessor() {
    }

    /**
     * 内置用户登录时获取客户端所有资源
     */
    public static List<Resource> getResources(String clientId, String signPassword) {
        ResourceClient resourceClient = getBean(FeignClientFactory.class).getFeignClient(ResourceClient.class, SERVICE_ID);
        return getData(resourceClient.getResources(clientId, signPassword));
    }

}
