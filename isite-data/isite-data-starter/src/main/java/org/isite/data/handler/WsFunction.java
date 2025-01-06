package org.isite.data.handler;

import org.isite.commons.cloud.data.vo.Result;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@FunctionalInterface
public interface WsFunction<P, R> {
    /**
     * 处理用户的请求数据，返回统一结构的 Result 响应结果
     *
     * @param data 请求数据
     * @return 响应结果
     */
    Result<R> apply(P data);
}
