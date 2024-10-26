package org.isite.security.web.filter;

import org.isite.security.support.DataAuthorityAssert;
import org.isite.security.support.DataAuthorityConfig;
import org.isite.security.web.exception.OverstepAccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.lang.String.format;
import static org.isite.commons.cloud.utils.PropertyUtils.getApplicationName;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.security.web.utils.SecurityUtils.getOauthUser;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @Description DataAuthorityFilter 读取当前用户的数据接口权限，校验当前请求是否被授权。
 * 在用户登录的时候，根据（RBAC）授权的资源ID查询接口路径（数据权限），缓存到用户登录信息中。
 * 如果配置文件中未进行属性配置或为false：security.data.authority.enabled=false，则DataAuthorityFilter不生效。
 * 如果是类似公众号，所有用户访问相同页面，则不需要进行数据（接口）权限控制，所有用户权限一样。
 * 如果只查询当前登录用户数据获公共数据，接口路径约定/my/**、public/**，(白名单)自动放行。
 * 数据（接口）权限控制，在token校验拦截器ResourceServerConfiguration(order = 3)之后执行，@see EnableResourceServer。
 * @Author <font color='blue'>zhangcm</font>
 */
@Order(100)
@WebFilter(filterName="dataAuthorityFilter", urlPatterns = "/*")
@ConditionalOnBean(DataAuthorityConfig.class)
public class DataAuthorityFilter implements Filter {

    private DataAuthorityAssert dataAuthorityAssert;
    private OverstepAccessHandler overstepAccessHandler;

    /**
     * chain.doFilter将请求转发给过滤器链下一个filter , 如果没有filter那就是你请求的资源。
     * 如果在doFilter()方法中，不写chain.doFilter()，业务无法继续往下处理。
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = cast(request);
        /*
         * 假定web application名称为news,在浏览器中输入请求路径：http://localhost:8080/news/main/list.jsp
         * 1、request.getContextPath() 可返回站点的根路径。也就是项目的名字：/news
         * 2、request.getServletPath()：/main/list.jsp
         * 3、request.getRequestURI()：/news/main/list.jsp
         * 4、request.getRealPath("/")：F:\Tomcat 6.0\webapps\news\test
         */
        String serviceId = getApplicationName();
        if (dataAuthorityAssert.isAuthorized(getOauthUser(), serviceId, httpRequest.getMethod(), httpRequest.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }
        //返回未授权异常信息
        overstepAccessHandler.handle(httpRequest, cast(response), new AccessDeniedException(
                format("%s %s#%s", FORBIDDEN.getReasonPhrase(), serviceId, httpRequest.getServletPath())));
    }

    @Autowired
    public void setDataAuthorityAssert(DataAuthorityAssert dataAuthorityAssert) {
        this.dataAuthorityAssert = dataAuthorityAssert;
    }

    @Autowired
    public void setOverstepAccessHandler(OverstepAccessHandler overstepAccessHandler) {
        this.overstepAccessHandler = overstepAccessHandler;
    }
}