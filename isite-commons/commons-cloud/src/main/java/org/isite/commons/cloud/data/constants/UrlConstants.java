package org.isite.commons.cloud.data.constants;

/**
 * @Description URL常量类
 * url常量命名规则约定：API_/MY_/PUBLIC_ + HTTP Method + 资源Path
 * @Author <font color='blue'>zhangcm</font>
 */
public class UrlConstants {

    private UrlConstants() {
    }

    /**
     * /api/**：api打头的接口不校验token和接口权限
     * /my/**: my打头的接口不校验接口权限（当前用户只能访问自己的数据），但需要校验token才可以访问
     * /public/**: 公共接口不校验接口权限（所有用户都能访问的数据），但需要校验token才可以访问
     */
    public static final String URL_API = "/api";
    public static final String URL_PUBLIC = "/public";
    public static final String URL_MY = "/my";

    public static final String URL_TEMPLATES = "/templates";
    public static final String URL_HTML_PC_PATTERN = URL_TEMPLATES + "/pc/%s.html";
    public static final String URL_HTML_APP_PATTERN = URL_TEMPLATES + "/app/%s.html";
    public static final String URL_STATIC_JS = "classpath:/static/js/";
    public static final String URL_STATIC_CSS = "classpath:/static/css/";
    public static final String URL_STATIC_IMAGES = "classpath:/static/images/";
    public static final String URL_CSS_PREFIX = "/css/**";
    public static final String URL_JS_PREFIX = "/js/**";
    public static final String URL_IMAGES_PREFIX = "/images/**";
}
