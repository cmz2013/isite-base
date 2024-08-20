package org.isite.commons.web.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;

/**
 * @Description 在Web开发中，通过servlet打通前后端。Spring MVC框架是围绕DispatcherServlet来设计的，DispatcherServlet继承HttpServlet，这个Servlet会把请求分发给各个处理器，
 * 并支持可配置的处理器映射、视图渲染、本地化、时区与主题渲染和文件上传等功能。service层或者某个工具类中需要获取到HttpServletRequest对象还是比较常见的。
 * 一种方式是将HttpServletRequest作为方法的参数从controller层一直放下传递，不过这种有点费劲，且做起来不是优雅；
 * 还有另一种则是RequestContextHolder，直接在需要用的地方使用如下方式取HttpServletRequest即可。
 * @Author <font color='blue'>zhangcm</font>
 */
public class RequestUtils {

    private RequestUtils() {
    }

    public static HttpServletRequest getRequest() {
        // 通过ThreadLocal的应用取到当前的HttpServletRequest
        RequestAttributes requestAttributes = getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        throw new RuntimeException("Unable to get the current request.");
    }

    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getResponse();
        }
        throw new RuntimeException("Unable to get the current response.");
    }

}
