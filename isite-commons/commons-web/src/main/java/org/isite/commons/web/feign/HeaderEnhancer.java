package org.isite.commons.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.cloud.constants.CloudConstants.X_VERSION;
import static org.isite.commons.lang.http.HttpHeaders.AUTHORIZATION;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getAuthorization;
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
    }
}