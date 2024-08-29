package org.isite.security.support;

import org.isite.security.data.vo.DataAuthority;
import org.isite.security.data.vo.OauthEmployee;
import org.springframework.util.PathMatcher;

import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.data.Constants.COMMA;

/**
 * @Description 检查用户数据接口权限
 * @Author <font color='blue'>zhangcm</font>
 */
public class DataAuthorityAssert {
    /**
     * 不需要登录认证就可以访问的接口
     */
    private String[] oauthPermits;
    /**
     * 只要通过登录认证就可以访问的接口
     */
    private String[] dataPermits;
    /**
     * 用于路径比对。在SpringBoot Web项目中，WebMvcAdapter#configurePathMatch自定义了PathMatcher
     */
    private final PathMatcher pathMatcher;

    public DataAuthorityAssert(PathMatcher pathMatcher, String oauthPermits, String dataPermits) {
        this.pathMatcher = pathMatcher;
        if (isNotBlank(oauthPermits)) {
            this.oauthPermits = oauthPermits.split(COMMA);
        }
        if (isNotBlank(dataPermits)) {
            this.dataPermits = dataPermits.split(COMMA);
        }
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

    /**
     * 只要通过登录认证就可以访问的接口
     */
    private boolean isDataPermits(String requestPath) {
        if (isEmpty(this.dataPermits)) {
            return false;
        }
        for (String permit : this.dataPermits) {
            if (this.pathMatcher.match(permit, requestPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验当前登录用户的数据（接口）权限
     */
    public boolean isAuthorized(
            OauthEmployee employee, String serviceId, String method, String requestPath) {
        if (isOauthPermits(requestPath)) {
            return TRUE;
        }
        if (isDataPermits(requestPath)) {
            return TRUE;
        }
        if (null == employee) {
            return FALSE;
        }
        if (TRUE.equals(employee.getInternal())) {
            return TRUE;
        }
        Set<DataAuthority> authorities = isBlank(serviceId) ? null : employee.getAuthorities(serviceId);
        if (isEmpty(authorities)) {
            return FALSE;
        }
        for (DataAuthority authority : authorities) {
            if (checkHttpMethod(authority, method) &&
                    this.pathMatcher.match(authority.getAuthority(), requestPath)) {
                return TRUE;
            }
        }
        return FALSE;
    }

    /**
     * 匹配HTTP方法
     */
    private boolean checkHttpMethod(DataAuthority authority, String method) {
        return null == authority.getMethod() || authority.getMethod().name().equalsIgnoreCase(method);
    }

}
