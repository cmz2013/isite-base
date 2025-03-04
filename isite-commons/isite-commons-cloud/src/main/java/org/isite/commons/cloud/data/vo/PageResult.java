package org.isite.commons.cloud.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.lang.enums.ResultStatus;

import java.util.List;
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
        return new PageResult<>(request, ResultStatus.OK.getCode(), list, total);
    }
}