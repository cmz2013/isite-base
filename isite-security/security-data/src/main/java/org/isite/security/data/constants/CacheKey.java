package org.isite.security.data.constants;

import static org.isite.commons.lang.Constants.BLANK_STRING;

/**
 * @Description 缓存KEY
 * @Author <font color='blue'>zhangcm</font>
 */
public class CacheKey {

    private CacheKey() {
    }

    /**
     * 认证鉴权中心缓存key的前缀
     */
    public static final String SECURITY_PREFIX = BLANK_STRING;
    /**
     * 用户登录失败次数key: login:times:{username}。不区分端信息
     */
    public static final String LOGIN_TIMES_FORMAT = SECURITY_PREFIX + "login:times:%s";
    /**
     * 验证码缓存key: code:{终端类型}:{终端标识}
     */
    public static final String VERIFY_CODE_FORMAT = SECURITY_PREFIX + "verifyCode:%s:%s";
    /**
     * 账号被锁key：login:locked:{username}。不区分端信息
     */
    public static final String LOGIN_LOCKED_FORMAT = SECURITY_PREFIX + "login:locked:%s";

}
