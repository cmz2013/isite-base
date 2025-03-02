package org.isite.data.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.data.support.enums.WsType;
import org.isite.data.support.vo.DataApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.isite.commons.web.feign.SignInterceptor.FEIGN_SIGN_PASSWORD;
import static org.isite.data.support.constants.DataUrls.API_GET_RPC;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "dataApiClient", value = SERVICE_ID)
public interface DataApiClient {
    /**
     * 执行器调用数据接口
     */
    @GetMapping(value = API_GET_RPC)
    Result<DataApi> callApi(@PathVariable("wsType") WsType wsType,
                            @PathVariable("apiId") String apiId,
                            @RequestHeader(FEIGN_SIGN_PASSWORD) String signPassword);
}
