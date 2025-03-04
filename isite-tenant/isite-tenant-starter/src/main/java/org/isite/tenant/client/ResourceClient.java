package org.isite.tenant.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.feign.SignInterceptor;
import org.isite.tenant.data.constants.TenantUrls;
import org.isite.tenant.data.vo.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "resourceClient", value = SERVICE_ID)
public interface ResourceClient {

    /**
     * 内置用户登录时获取客户端所有资源
     */
    @GetMapping(value = TenantUrls.API_GET_RESOURCES)
    Result<List<Resource>> getResources(
            @RequestParam("clientId") String clientId,
            @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);
}
