package org.isite.commons.web.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.isite.commons.cloud.data.constants.HttpHeaders.AUTHORIZATION;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_EMPLOYEE_ID;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_TENANT_ID;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_USER_ID;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_VERSION;

/**
 * @Description 保存请求头数据（敏感信息），用于后续请求使用。
 * spring-webmvc 是基于 Servlet API，使用阻塞式 I/O，适用于传统的同步请求处理，可以把数据封装在ThreadLocal中传递。
 * ThreadLocal 是Java提供的一种线程绑定机制，用于在多线程环境中为每个线程存储独立的数据副本，从而避免了线程安全问题。
 * Alibaba提供的TransmittableThreadLocal用于解决在异步执行（如线程池）时，线程变量传递的问题。
 * @Author <font color='blue'>zhangcm</font>
 */
public class TransmittableHeaders implements HandlerInterceptor {

    private static final ThreadLocal<String> TRANSMITTABLE_VERSION = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> TRANSMITTABLE_AUTHORIZATION = new TransmittableThreadLocal<>();
    private static final ThreadLocal<Long> TRANSMITTABLE_USER_ID = new TransmittableThreadLocal<>();
    private static final ThreadLocal<Integer> TRANSMITTABLE_EMPLOYEE_ID = new TransmittableThreadLocal<>();
    private static final ThreadLocal<Integer> TRANSMITTABLE_TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 获取当前请求的授权信息（Bearer Token等）
     */
    public static String getAuthorization() {
        return TRANSMITTABLE_AUTHORIZATION.get();
    }

    /**
     * 获取当前请求的服务版本号
     */
    public static String getVersion() {
        return TRANSMITTABLE_VERSION.get();
    }

    /**
     * @Description 获取当前登录的用户ID。网关校验token通过后，会在请求头添加当前登录的用户ID。
     * 调用该方法时需要注意：
     * 1）如果后端服务接入认证鉴权中心校验token，可以调用SecurityUtils从SecurityContext中获取用户信息，该方法返回null
     * 2）如果只查询当前登录用户数据，接口路径约定/my/**，数据接口授权拦截器自动放行。
     */
    public static Long getUserId() {
        return TRANSMITTABLE_USER_ID.get();
    }

    /**
     * @Description 获取当前登录的员工ID。网关校验token通过后，会在请求头添加当前登录的员工ID。
     * 调用该方法时需要注意：
     * 1）如果后端服务接入认证鉴权中心校验token，可以调用SecurityUtils从SecurityContext中获取用户信息，该方法返回null
     * 2）如果只查询当前登录用户数据，接口路径约定/my/**，数据接口授权拦截器自动放行。
     */
    public static Integer getEmployeeId() {
        return TRANSMITTABLE_EMPLOYEE_ID.get();
    }

    /**
     * @Description 获取当前登录的员工的租户ID。网关校验token通过后，会在请求头添加当前登录的租户ID。
     * 调用该方法时需要注意：如果后端服务接入认证鉴权中心校验token，可以调用SecurityUtils从SecurityContext中获取用户信息，该方法返回null
     */
    public static Integer getTenantId() {
        return TRANSMITTABLE_TENANT_ID.get();
    }

    /**
     * @Description 在请求处理之前进行调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String version = request.getHeader(X_VERSION);
        if (isNotBlank(version)) {
            TRANSMITTABLE_VERSION.set(version);
        }
        String authorization = request.getHeader(AUTHORIZATION);
        if (isNotBlank(authorization)) {
            TRANSMITTABLE_AUTHORIZATION.set(authorization);
        }
        String userId = request.getHeader(X_USER_ID);
        if (isNotBlank(userId)) {
            TRANSMITTABLE_USER_ID.set(parseLong(userId));
        }
        String employeeId = request.getHeader(X_EMPLOYEE_ID);
        if (isNotBlank(employeeId)) {
            TRANSMITTABLE_EMPLOYEE_ID.set(parseInt(employeeId));
        }
        String tenantId = request.getHeader(X_TENANT_ID);
        if (isNotBlank(tenantId)) {
            TRANSMITTABLE_TENANT_ID.set(parseInt(tenantId));
        }
        //如果false，停止流程，api被拦截
        return true;
    }

    /**
     * 在请求完成之后进行调用
     */
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        /*
         * web容器使用了线程池，当一个请求使用完某个线程，该线程会放回线程池被其它请求使用，
         * 这就导致一个问题，不同的请求还是有可能会使用到同一个线程，而ThreadLocal是属于线程的，
         * 如果我们使用完ThreadLocal对象而没有手动删掉，那么后面的请求就有机会使用到被使用过的ThreadLocal对象
         */
        TRANSMITTABLE_VERSION.remove();
        TRANSMITTABLE_AUTHORIZATION.remove();
        TRANSMITTABLE_USER_ID.remove();
        TRANSMITTABLE_EMPLOYEE_ID.remove();
        TRANSMITTABLE_TENANT_ID.remove();
    }
}
