package org.isite.security.gateway.client;

import org.isite.commons.lang.data.Result;
import org.isite.security.data.oauth.OauthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

import static org.isite.security.data.constants.SecurityConstants.SERVICE_ID;

/**
 * @Description 网关服务器调用认证服务器接口校验token和client信息，并返回用户信息.
 * Feign客户端的名称通常用于服务发现和负载均衡，而URL则用于直接指定目标服务的地址。
 * 当你同时设置name和url时，Feign客户端会优先使用url属性指定的地址，而忽略name属性的服务发现功能。
 * @Author <font color='blue'>zhangcm</font>
 */
@FeignClient(name = SERVICE_ID, url = "${security.oauth2.resource.user-info-uri}")
public interface OauthUserClient {

    @GetMapping
    Result<OauthUser> getUser(@RequestHeader Map<String, String> headers);
}
