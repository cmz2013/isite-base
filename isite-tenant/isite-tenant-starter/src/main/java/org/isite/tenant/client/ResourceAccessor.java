package org.isite.tenant.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.data.vo.Resource;

import java.util.List;
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
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        ResourceClient resourceClient = feignClientFactory.getFeignClient(ResourceClient.class, TenantConstants.SERVICE_ID);
        return ResultUtils.getData(resourceClient.getResources(clientId, signPassword));
    }

}
