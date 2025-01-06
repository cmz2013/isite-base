package org.isite.commons.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.constants.HttpHeaders.AUTHORIZATION;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_EMPLOYEE_ID;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_TENANT_ID;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_USER_ID;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_VERSION;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getAuthorization;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getEmployeeId;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getVersion;

/**
 * @Description 通过FeignClientFactory创建的client，添加自定义请求头。
 * @Author <font color='blue'>zhangcm</font>
 */
public class HeaderEnhancer implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String version = getVersion();
        if (isNotBlank(version)) {
            template.header(X_VERSION, version);
        }
        String authorization = getAuthorization();
        if (isNotBlank(authorization)) {
            template.header(AUTHORIZATION, authorization);
        }
        Long userId = getUserId();
        if (null!= userId) {
            template.header(X_USER_ID, userId.toString());
        }
        Long employeeId = getEmployeeId();
        if (null!= employeeId) {
            template.header(X_EMPLOYEE_ID, employeeId.toString());
        }
        Integer tenantId = getTenantId();
        if (null!= tenantId) {
            template.header(X_TENANT_ID, tenantId.toString());
        }
    }
}