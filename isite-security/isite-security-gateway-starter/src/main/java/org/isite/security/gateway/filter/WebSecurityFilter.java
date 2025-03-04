package org.isite.security.gateway.filter;

import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.converter.ResultConverter;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Error;
import org.isite.commons.lang.enums.ResultStatus;
import org.isite.commons.lang.json.Jackson;
import org.isite.gateway.support.GatewayUtils;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.gateway.client.OauthUserClient;
import org.isite.security.support.DataAuthorityAssert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * @Description 校验token和数据接口权限。
 * 在用户登录的时候，根据（RBAC）授权的资源ID查询接口路径（数据权限），缓存到用户登录信息中。
 * 如果配置文件中未进行属性配置或为false：security.data.authority.enabled=false，则不校验数据接口权限。
 * 如果只查询当前登录用户数据获公共数据，接口路径约定/my/**、public/**，(白名单)自动放行。
 * @Author <font color='blue'>zhangcm</font>
 */
public class WebSecurityFilter implements GatewayFilter, Ordered, InitializingBean {
    //不需要登录认证就可以访问的接口
    private String[] oauthPermits;
    @Setter
    private OauthUserClient oauthUserClient;
    private DataAuthorityAssert dataAuthorityAssert;
    @Setter
    private PathMatcher pathMatcher;

    public void setDataAuthorityAssert(@Nullable DataAuthorityAssert dataAuthorityAssert) {
        this.dataAuthorityAssert = dataAuthorityAssert;
    }

    public WebSecurityFilter(String oauthPermits) {
        if (StringUtils.isNotBlank(oauthPermits)) {
            this.oauthPermits = oauthPermits.split(Constants.COMMA);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        if (isOauthPermits(requestPath)) {
            return chain.filter(exchange);
        }
        MultiValueMap<String, String> headers = request.getHeaders();
        if (StringUtils.isBlank(headers.getFirst(HttpHeaders.AUTHORIZATION))) {
            return doResponse(exchange, new Error(
                    ResultStatus.UNAUTHORIZED.getCode(), ResultStatus.UNAUTHORIZED.getReasonPhrase()));
        }
        String serviceId = GatewayUtils.getServiceId(exchange);
        OauthUser oauthUser = oauthUserClient.getUserDetails(headers.toSingleValueMap());
        if (null == dataAuthorityAssert || dataAuthorityAssert.isAuthorized(
                oauthUser, serviceId, request.getMethodValue(), requestPath)) {
            headers.add(HttpHeaders.X_USER_ID, String.valueOf(oauthUser.getUserId()));
            if (null != oauthUser.getTenant()) {
                headers.add(HttpHeaders.X_TENANT_ID, String.valueOf(oauthUser.getTenant().getId()));
                headers.add(HttpHeaders.X_EMPLOYEE_ID, String.valueOf((oauthUser.getEmployeeId())));
            }
            return chain.filter(exchange);
        }
        return doResponse(exchange, new Error(ResultStatus.FORBIDDEN.getCode(),
                String.format("%s %s#%s", ResultStatus.FORBIDDEN.getReasonPhrase(), serviceId, requestPath)));
    }

    /**
     * 不需要登录认证就可以访问的接口
     */
    private boolean isOauthPermits(String requestPath) {
        if (ArrayUtils.isEmpty(this.oauthPermits)) {
            return Boolean.FALSE;
        }
        for (String permit : this.oauthPermits) {
            if (this.pathMatcher.match(permit, requestPath)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Mono<Void> doResponse(ServerWebExchange exchange, Error error) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        // header set_json响应
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //返回异常原因给前端
        return response.writeWith(Mono.fromSupplier(() -> response.bufferFactory()
                .wrap(Jackson.toJsonString(ResultConverter.toResult(error)).getBytes())));
    }

    @Override
    public int getOrder() {
        // 设置过滤器的执行顺序，数值越小优先级越高。
        return -1;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.oauthUserClient, "oauthUserClient must be set");
        Assert.notNull(this.pathMatcher, "pathMatcher must be set");
    }
}
