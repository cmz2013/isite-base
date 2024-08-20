package org.isite.commons.lang.utils;

import org.isite.commons.lang.Error;
import org.isite.commons.lang.data.Result;

import static org.isite.commons.lang.Assert.isTrue;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ResultUtils {

    private ResultUtils() {
    }

    /**
     * 2xx 代表请求已成功被服务器接收、理解、并接受
     * @return true: 成功, false: 失败
     */
    public static boolean isOk(Result<?> result) {
        return result.getCode() >= 200 && result.getCode() <= 299;
    }

    /**
     * @Description 当Result状态为成功时返回数据，否则抛出异常信息
     */
    public static <T> T getData(Result<T> result) {
        isTrue(isOk(result), new Error(result.getCode(), result.getMessage()));
        return result.getData();
    }

    /**
     * @Description 当Result状态为成功时返回数据，否则抛出message异常信息
     */
    public static <T> T getData(Result<T> result, String message) {
        isTrue(isOk(result), new Error(result.getCode(), message));
        return result.getData();
    }
}
