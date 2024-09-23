package org.isite.tenant.client;

import org.isite.commons.cloud.data.Result;
import org.isite.tenant.data.vo.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static org.isite.commons.cloud.feign.SignInterceptor.FEIGN_SIGN_PASSWORD;
import static org.isite.tenant.data.constants.UrlConstants.API_GET_CLIENT_RESOURCES;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "resourceClient", value = SERVICE_ID)
public interface ResourceClient {

    /**
     * 内置用户登录时获取客户端所有资源
     */
    @GetMapping(value = API_GET_CLIENT_RESOURCES)
    Result<List<Resource>> getResources(
            @PathVariable("clientId") String clientId,
            @RequestHeader(FEIGN_SIGN_PASSWORD) String signPassword);
}
