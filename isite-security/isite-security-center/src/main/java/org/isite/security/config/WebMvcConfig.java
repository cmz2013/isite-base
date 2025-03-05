package org.isite.security.config;

import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.web.config.WebMvcAdapter;
import org.isite.imports.EnableWeb;
import org.isite.security.data.constants.SecurityUrls;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@EnableWeb
@Configuration
public class WebMvcConfig extends WebMvcAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SecurityUrls.URL_OAUTH + UrlConstants.URL_JS_PREFIX)
                .addResourceLocations(UrlConstants.URL_STATIC_JS);
        registry.addResourceHandler(SecurityUrls.URL_OAUTH + UrlConstants.URL_CSS_PREFIX)
                .addResourceLocations(UrlConstants.URL_STATIC_CSS);
        registry.addResourceHandler(SecurityUrls.URL_OAUTH + UrlConstants.URL_IMAGES_PREFIX)
                .addResourceLocations(UrlConstants.URL_STATIC_IMAGES);
    }
}