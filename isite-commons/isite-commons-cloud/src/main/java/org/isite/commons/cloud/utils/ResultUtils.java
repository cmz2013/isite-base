package org.isite.commons.cloud.utils;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Error;
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
        return null != result && result.getCode() >= 200 && result.getCode() <= 299;
    }

    /**
     * @Description 当Result状态为成功时返回数据，否则抛出异常信息
     */
    public static <T> T getData(Result<T> result) {
        Assert.isTrue(isOk(result), new Error(result.getCode(), result.getMessage()));
        return result.getData();
    }

    /**
     * @Description 当Result状态为成功时返回数据，否则抛出message异常信息
     */
    public static <T> T getData(Result<T> result, String message) {
        Assert.isTrue(isOk(result), new Error(result.getCode(), message));
        return result.getData();
    }
}
