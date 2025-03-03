package org.isite.security.data.constants;

import org.isite.commons.lang.Constants;

/**
 * @Description 缓存KEY
 * @Author <font color='blue'>zhangcm</font>
 */
public class CacheKeys {

    private CacheKeys() {
    }

    /**
     * 认证鉴权中心缓存key的前缀
     */
    public static final String SECURITY_PREFIX = Constants.BLANK_STR;
    /**
     * 用户登录失败次数key: login:times:{username}。不区分端信息
     */
    public static final String LOGIN_TIMES_FORMAT = SECURITY_PREFIX + "LOGIN:TIMES:%s";
    /**
     * 验证码缓存key: code:{终端类型}:{终端标识}
     */
    public static final String VERIFICATION_CODE_FORMAT = SECURITY_PREFIX + "VERIFICATION:CODE:%s:%s";
    /**
     * 账号被锁key：login:locked:{username}。不区分端信息
     */
    public static final String LOGIN_LOCKED_FORMAT = SECURITY_PREFIX + "LOGIN:LOCKED:%s";

}
