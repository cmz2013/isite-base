package org.isite.security.support;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.Constants;
import org.isite.security.data.vo.DataAuthority;
import org.isite.security.data.vo.OauthUser;
import org.springframework.util.PathMatcher;

import java.util.Set;
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
        if (StringUtils.isNotBlank(oauthPermits)) {
            this.oauthPermits = oauthPermits.split(Constants.COMMA);
        }
        if (StringUtils.isNotBlank(dataPermits)) {
            this.dataPermits = dataPermits.split(Constants.COMMA);
        }
    }

    /**
     * 不需要登录认证就可以访问的接口
     */
    private boolean isOauthPermits(String requestPath) {
        if (ArrayUtils.isEmpty(this.oauthPermits)) {
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
        if (ArrayUtils.isEmpty(this.dataPermits)) {
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
            OauthUser oauthUser, String serviceId, String method, String requestPath) {
        if (isOauthPermits(requestPath)) {
            return Boolean.TRUE;
        }
        if (isDataPermits(requestPath)) {
            return Boolean.TRUE;
        }
        if (null == oauthUser) {
            return Boolean.FALSE;
        }
        if (Boolean.TRUE.equals(oauthUser.getInternal())) {
            return Boolean.TRUE;
        }
        Set<DataAuthority> authorities = oauthUser.getAuthorities(serviceId);
        if (CollectionUtils.isEmpty(authorities)) {
            return Boolean.FALSE;
        }
        for (DataAuthority authority : authorities) {
            if (checkHttpMethod(authority, method) &&
                    this.pathMatcher.match(authority.getAuthority(), requestPath)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 匹配HTTP方法
     */
    private boolean checkHttpMethod(DataAuthority authority, String method) {
        return null == authority.getMethod() || authority.getMethod().name().equalsIgnoreCase(method);
    }

}
