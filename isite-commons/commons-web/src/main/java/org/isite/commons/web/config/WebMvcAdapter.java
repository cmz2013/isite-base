package org.isite.commons.web.config;

import org.isite.commons.cloud.converter.EnumConverter;
import org.isite.commons.cloud.utils.RequestPathMatcher;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.PathMatcher;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.json.Jackson.OBJECT_MAPPER;

/**
 * @Description 可以在项目配置类中继承使用 WebMvcAdapter
 * @Author <font color='blue'>zhangcm</font>
 */
public class WebMvcAdapter extends WebMvcConfigurationSupport {

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //MappingJackson2HttpMessageConverter#DEFAULT_CHARSET = UTF_8
        converters.add(new MappingJackson2HttpMessageConverter(OBJECT_MAPPER));
        converters.add(new StringHttpMessageConverter(UTF_8));
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TransmittableHeaders()).addPathPatterns("/**");
    }

    @Bean
    public PathMatcher pathMatcher() {
        return new RequestPathMatcher();
    }

    /**
     * Spring提供的路径匹配工具：AntPathMatcher
     */
    @Override
    protected void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPathMatcher(pathMatcher());
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConverter());
    }

    /**
     * @Description SpringMVC 数组和集合默认只能接收到256个数据,可以通过以下方式进行配置：
     * 1) 在controller类中添加如下方法，在方法上添加注解@InitBinder，@InitBinder是类初始化时调用的方法注解。这样只对当前类的配置有效
     * void initBinder(WebDataBinder binder) { binder.setAutoGrowCollectionLimit(THOUSAND); }
     * 2) 使用ConfigurableWebBindingInitializer全局更改相应配置，针对所有controller的类都配置有效
     */
    @Override
    protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer(
            FormattingConversionService conversionService, Validator validator) {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer() {
            @Override
            public void initBinder(WebDataBinder binder) {
                super.initBinder(binder);
                binder.setAutoGrowCollectionLimit(THOUSAND);
            }
        };
        initializer.setConversionService(conversionService);
        initializer.setValidator(validator);
        MessageCodesResolver messageCodesResolver = getMessageCodesResolver();
        if (messageCodesResolver != null) {
            initializer.setMessageCodesResolver(messageCodesResolver);
        }
        return initializer;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        applicationContext.getBean(ResourceBundleMessageSource.class).addBasenames("i18n-web/messages");
    }

}