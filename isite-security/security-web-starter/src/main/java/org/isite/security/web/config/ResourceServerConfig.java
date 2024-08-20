package org.isite.security.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @Description 定义配置类,使用@EnableResourceServer启用OAuth2。
 * spring-webmvc 是 Spring 框架中用于构建传统 Servlet 栈 Web 应用程序的模块。它基于 Servlet API，使用阻塞式 I/O，适用于传统的同步请求处理。
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerSupport {
}
