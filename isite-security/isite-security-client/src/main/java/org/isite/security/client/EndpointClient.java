package org.isite.security.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.security.data.constants.SecurityUrls;
import org.isite.security.data.vo.OauthClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public interface EndpointClient {

    @GetMapping(SecurityUrls.GET_OAUTH_CLIENTS)
    Result<List<OauthClient>> findOauthClients(@RequestParam("clientIds") List<String> clientIds);
}
