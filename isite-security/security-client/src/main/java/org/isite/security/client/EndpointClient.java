package org.isite.security.client;

import org.isite.commons.lang.data.Result;
import org.isite.security.data.vo.OauthClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static org.isite.security.data.constants.UrlConstants.GET_OAUTH_CLIENTS;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public interface EndpointClient {

    @GetMapping(GET_OAUTH_CLIENTS)
    Result<List<OauthClient>> findOauthClients(@RequestParam("clientIds") Set<String> clientIds);
}
