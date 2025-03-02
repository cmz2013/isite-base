package org.isite.data.support.constants;

/**
 * @Description 缓存KEY
 * @Author <font color='blue'>zhangcm</font>
 */
public class CacheKeys {

    private CacheKeys() {
    }

    /**
     * 启用的接口数量
     */
    public static final String DATA_API_NUMBER = "DATA:API:NUMBER";
    /**
     * 执行器数量
     */
    public static final String DATA_EXECUTOR_NUMBER = "DATA:EXECUTOR:NUMBER";
    /**
     * 接口调用次数前缀
     */
    public static final String DATA_CALL_PREFIX = "DATA:CALL:";
    /**
     * 接口最近1小时调用详情（每分钟的调用次数）
     */
    public static final String DATA_CALL_LATEST = "LATEST";
    /**
     * 接口最近1小时调用失败次数(每分钟的失败日志个数)
     */
    public static final String DATA_CALL_FAILURE = "FAILURE";

    public static final String LOCK_DATA_LOG = "LOCK:DATA:LOG";
}
