package org.isite.security.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.security.data.vo.OauthClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.isite.security.data.constants.SecurityUrls.GET_OAUTH_CLIENTS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public interface EndpointClient {

    @GetMapping(GET_OAUTH_CLIENTS)
    Result<List<OauthClient>> findOauthClients(@RequestParam("clientIds") List<String> clientIds);
}
