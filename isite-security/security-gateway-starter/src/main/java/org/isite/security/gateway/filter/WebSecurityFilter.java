package org.isite.security.gateway.filter;

import lombok.Setter;
import org.isite.commons.cloud.data.Result;
import org.isite.commons.lang.Error;
import org.isite.security.data.vo.OauthEmployee;
import org.isite.security.data.vo.OauthUser;
import org.isite.security.gateway.client.OauthUserClient;
import org.isite.security.support.DataAuthorityAssert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.constants.HttpHeaders.AUTHORIZATION;
import static org.isite.commons.cloud.constants.HttpHeaders.X_CLIENT_ID;
import static org.isite.commons.cloud.constants.HttpHeaders.X_EMPLOYEE_ID;
import static org.isite.commons.cloud.constants.HttpHeaders.X_TENANT_ID;
import static org.isite.commons.cloud.constants.HttpHeaders.X_USER_ID;
import static org.isite.commons.cloud.data.Converter.toResult;
import static org.isite.commons.cloud.utils.ResultUtils.isOk;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.COMMA;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.gateway.support.GatewayUtils.getServiceId;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.fromSupplier;

/**
 * @Description 校验token和数据接口权限。
 * 在用户登录的时候，根据（RBAC）授权的资源ID查询接口路径（数据权限），缓存到用户登录信息中。
 * 如果配置文件中未进行属性配置或为false：security.data.authority.enabled=false，则不校验数据接口权限。
 * 如果只查询当前登录用户数据获公共数据，接口路径约定/my/**、public/**，(白名单)自动放行。
 * @Author <font color='blue'>zhangcm</font>
 */
public class WebSecurityFilter implements GatewayFilter, Ordered, InitializingBean {
    /**
     * 不需要登录认证就可以访问的接口
     */
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
        if (isNotBlank(oauthPermits)) {
            this.oauthPermits = oauthPermits.split(COMMA);
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        if (isOauthPermits(requestPath)) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = request.getHeaders();
        if (isBlank(headers.getFirst(AUTHORIZATION))) {
            return doResponse(exchange, new Error(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase()));
        }
        Result<OauthUser> result = oauthUserClient.getUser(headers.toSingleValueMap());
        if (!isOk(result)) {
            return doResponse(exchange, new Error(result.getCode(), result.getMessage()));
        }
        OauthUser oauthUser = result.getData();
        OauthEmployee oauthEmployee = oauthUser instanceof OauthEmployee ? (OauthEmployee) oauthUser : null;
        String serviceId = getServiceId(exchange);

        if (null == dataAuthorityAssert || dataAuthorityAssert.isAuthorized(
                oauthEmployee, serviceId, request.getMethodValue(), requestPath)) {
            headers.add(X_USER_ID, valueOf(oauthUser.getUserId()));
            if (null != oauthEmployee) {
                headers.add(X_EMPLOYEE_ID, valueOf((oauthEmployee.getEmployeeId())));
                headers.add(X_TENANT_ID, valueOf(oauthEmployee.getTenant().getId()));
            }
            headers.add(X_CLIENT_ID, oauthUser.getClientId());
            return chain.filter(exchange);
        }
        return doResponse(exchange, new Error(FORBIDDEN.value(),
                format("%s %s#%s", FORBIDDEN.getReasonPhrase(), serviceId, requestPath)));
    }

    /**
     * 不需要登录认证就可以访问的接口
     */
    private boolean isOauthPermits(String requestPath) {
        if (isEmpty(this.oauthPermits)) {
            return false;
        }
        for (String permit : this.oauthPermits) {
            if (this.pathMatcher.match(permit, requestPath)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> doResponse(ServerWebExchange exchange, Error error) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(OK);
        // header set_json响应
        response.getHeaders().setContentType(APPLICATION_JSON);
        //返回异常原因给前端
        return response.writeWith(fromSupplier(() -> response.bufferFactory().wrap(
                toJsonString(toResult(error)).getBytes())));
    }

    @Override
    public int getOrder() {
        // 设置过滤器的执行顺序，数值越小优先级越高。
        return -1;
    }

    @Override
    public void afterPropertiesSet() {
        notNull(this.oauthUserClient, "oauthUserClient must be set");
        notNull(this.pathMatcher, "pathMatcher must be set");
    }
}
