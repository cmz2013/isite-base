package org.isite.data.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.feign.SignInterceptor;
import org.isite.data.support.constants.DataUrls;
import org.isite.data.support.enums.WsType;
import org.isite.data.support.vo.DataApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "dataApiClient", value = SERVICE_ID)
public interface DataApiClient {
    /**
     * 执行器调用数据接口
     */
    @GetMapping(value = DataUrls.API_GET_RPC)
    Result<DataApi> callApi(@PathVariable("wsType") WsType wsType,
                            @PathVariable("apiId") String apiId,
                            @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);
}
