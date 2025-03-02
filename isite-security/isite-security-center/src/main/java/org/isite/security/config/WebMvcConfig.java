package org.isite.security.config;

import org.isite.commons.web.config.WebMvcAdapter;
import org.isite.imports.EnableWeb;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_CSS_PREFIX;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_IMAGES_PREFIX;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_JS_PREFIX;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_STATIC_CSS;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_STATIC_IMAGES;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_STATIC_JS;
import static org.isite.security.data.constants.SecurityUrls.URL_OAUTH;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@EnableWeb
@Configuration
public class WebMvcConfig extends WebMvcAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(URL_OAUTH + URL_JS_PREFIX).addResourceLocations(URL_STATIC_JS);
        registry.addResourceHandler(URL_OAUTH + URL_CSS_PREFIX).addResourceLocations(URL_STATIC_CSS);
        registry.addResourceHandler(URL_OAUTH + URL_IMAGES_PREFIX).addResourceLocations(URL_STATIC_IMAGES);
    }
}