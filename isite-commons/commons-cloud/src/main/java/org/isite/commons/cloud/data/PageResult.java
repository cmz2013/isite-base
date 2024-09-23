package org.isite.commons.cloud.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static org.isite.commons.lang.enums.ResultStatus.OK;

/**
 * @Description 分页查询响应数据
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PageResult<D> extends Result<List<D>> {
    /**
     * 数据总条数。根据total计算总页数，可在用户端处理，减轻服务端负载
     */
    private Long total;
    /**
     * 请求参数
     */
    private PageRequest<?> request;

    public PageResult(PageRequest<?> request, int code, List<D> list, Long total) {
        super(code, list);
        this.request = request;
        this.total = total;
    }

    public static <D> PageResult<D> success(PageRequest<?> request, List<D> list, Long total) {
        return new PageResult<>(request, OK.getCode(), list, total);
    }
}