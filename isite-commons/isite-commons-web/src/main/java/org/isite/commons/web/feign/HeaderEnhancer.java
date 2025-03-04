package org.isite.commons.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.web.interceptor.TransmittableHeaders;

/**
 * @Description 通过FeignClientFactory创建的client，添加自定义请求头。
 * @Author <font color='blue'>zhangcm</font>
 */
public class HeaderEnhancer implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String version = TransmittableHeaders.getVersion();
        if (StringUtils.isNotBlank(version)) {
            template.header(HttpHeaders.X_VERSION, version);
        }
        String authorization = TransmittableHeaders.getAuthorization();
        if (StringUtils.isNotBlank(authorization)) {
            template.header(HttpHeaders.AUTHORIZATION, authorization);
        }
        Long userId = TransmittableHeaders.getUserId();
        if (null!= userId) {
            template.header(HttpHeaders.X_USER_ID, userId.toString());
        }
        Long employeeId = TransmittableHeaders.getEmployeeId();
        if (null!= employeeId) {
            template.header(HttpHeaders.X_EMPLOYEE_ID, employeeId.toString());
        }
        Integer tenantId = TransmittableHeaders.getTenantId();
        if (null!= tenantId) {
            template.header(HttpHeaders.X_TENANT_ID, tenantId.toString());
        }
    }
}