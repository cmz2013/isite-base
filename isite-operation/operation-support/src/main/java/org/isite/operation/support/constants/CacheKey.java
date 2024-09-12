package org.isite.operation.support.constants;

/**
 * @Description 缓存KEY
 * @Author <font color='blue'>zhangcm</font>
 */
public class CacheKey {

    private CacheKey() {
    }

    /**
     * 使用活动ID作为运营活动缓存KEY
     */
    public static final String ACTIVITY_PREFIX = "ACTIVITY:";
    /**
     * 使用行为类型作为活动id缓存KEY
     */
    public static final String ACTIVITY_IDS_EVENT_PREFIX = "ACTIVITY:IDS:EVENT:";
    /**
     * 使用活动ID和用户终端类型作为活动网页源码缓存KEY
     */
    public static final String WEBPAGE_ACTIVITY_TERMINAL_PREFIX = "WEBPAGE:ACTIVITY:TERMINAL:";
    /**
     * 活动并发锁KEY。活动上架后不能编辑活动信息，防止并发造成数据错误
     * 活动并发锁用于防止多个用户，在编辑活动信息的时候同时上架；也可以防止多个用户同时编辑活动信息，并发场景导致数据异常
     */
    public static final String LOCK_ACTIVITY = "LOCK:ACTIVITY:${arg0}";
    /**
     * 运营活动奖品并发锁KEY
     */
    public static final String LOCK_PRIZE = "LOCK:PRIZE:${id}";
    /**
     * 用户抽奖并发锁KEY
     */
    public static final String LOCK_WHEEL_USER = "LOCK:WHEEL:${id}:USER:${arg1}";
    /**
     * 用户使用VIP积分并发锁KEY
     */
    public static final String LOCK_VIP_SCORE_USER = "LOCK:VIP:SCORE:USER:${arg0}";
    /**
     * 用户使用活动积分并发锁KEY
     */
    public static final String LOCK_ACTIVITY_SCORE_USER = "LOCK:ACTIVITY:${arg0}:SCORE:USER:${arg1}";
    /**
     * 用户签到并发锁KEY
     */
    public static final String LOCK_SIGN_USER = "LOCK:SIGN:USER:${arg0}";

}
