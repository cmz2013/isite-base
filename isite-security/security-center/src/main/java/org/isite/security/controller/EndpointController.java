package org.isite.security.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.config.EndpointConfig;
import org.isite.security.data.vo.OauthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.isite.security.data.constants.UrlConstants.GET_OAUTH_CLIENTS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class EndpointController extends BaseController {

    private EndpointConfig endpointConfig;

    @Autowired
    public void setEndpointConfig(EndpointConfig endpointConfig) {
        this.endpointConfig = endpointConfig;
    }

    @Validated
    @GetMapping(GET_OAUTH_CLIENTS)
    public Result<List<OauthClient>> findOauthClients(@NotEmpty @RequestParam("clientIds") List<String> clientIds) {
        //filter方法会返回一个新的流，其中包含符合过滤条件的元素，但原始集合保持不变。
        return toResult(endpointConfig.getClients().stream().filter(client -> clientIds.contains(client.getClientId()))
                .map(client -> new OauthClient(client.getClientId(), client.getClientName())).collect(toList()));
    }
}
