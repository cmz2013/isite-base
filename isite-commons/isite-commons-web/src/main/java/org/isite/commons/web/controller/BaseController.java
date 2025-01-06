package org.isite.commons.web.controller;

import org.isite.commons.cloud.data.dto.ListRequest;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.ListResult;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;

import java.util.List;

import static org.isite.commons.cloud.data.vo.ListResult.success;
import static org.isite.commons.cloud.data.vo.PageResult.success;

/**
 * @Description Controller公共方法
 * 注意：Controller方法建议不超过10行
 * @Author <font color='blue'>zhangcm</font>
 */
public class BaseController {
    /**
     * @Description 处理请求返回响应数据
     * @param vo 返回响应数据
     * @param <V> VO class
     */
    protected <V> Result<V> toResult(V vo) {
        return success(vo);
    }

    /**
     * @Description 处理列表请求返回响应数据
     * @param request 列表请求参数
     * @param vos 返回响应数据
     */
    protected <V> ListResult<V> toListResult(ListRequest<?> request, List<V> vos) {
        return success(request, vos);
    }

    /**
     * @Description 处理分页请求返回响应数据
     * @param request 分页请求参数
     * @param vos 返回响应数据
     */
    protected <V> PageResult<V> toPageResult(PageRequest<?> request, List<V> vos, long total) {
        return success(request, vos, total);
    }

    /**
     * @Description 处理请求返回成功状态
     * 注意：Runnable只是接口，在这里不是用于线程。Runnable用于实现线程时，可用 new Thread(Runnable).start() 或线程池启动执行线程
     */
    protected Result<Object> toResult(Runnable runnable) {
        runnable.run();
        return success();
    }
}