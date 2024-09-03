package org.isite.data.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.sign.Signed;
import org.isite.data.service.ExecutorSignSecret;
import org.isite.data.service.RpcService;
import org.isite.data.support.enums.WsType;
import org.isite.data.support.vo.DataApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.constants.CloudConstants.X_APP_CODE;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.data.support.constants.UrlConstants.API_GET_RPC;

/**
 * @Description 数据接口API
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class RpcController extends BaseController {
    /**
     * 接口调用Service
     */
    private RpcService rpcService;

    /**
     * @Description 查询要调用的数据接口
     * SpringMVC提供了@RequestHeader注解用于映射请求头数据到Controller方法的对应参数
     */
    @Signed(secret = ExecutorSignSecret.class)
    @GetMapping(API_GET_RPC)
    public Result<DataApi> callApi(@RequestHeader(X_APP_CODE) String appCode,
                                   @PathVariable("wsType") WsType wsType, @PathVariable("apiId") String apiId) {
        return toResult(convert(rpcService.callApi(appCode, wsType, apiId), DataApi::new));
    }

    @Autowired
    public void setDataRpcService(RpcService rpcService) {
        this.rpcService = rpcService;
    }
}
