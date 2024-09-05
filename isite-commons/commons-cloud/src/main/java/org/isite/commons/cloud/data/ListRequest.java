package org.isite.commons.cloud.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Description 根据有序的索引字段执行分页查询，不统计总条数和页数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ListRequest<Q> {
    /**
     * 在当前页的最小值或最大值，取决于排序方式。index为null时，查询第一页数据
     */
    private Object index;
    /**
     * 有序索引字段排序
     */
    @Valid
    @NotNull
    private OrderRequest order;
    /**
     * 每页条数
     */
    @NotNull
    private Integer pageSize;
    /**
     * 查询条件
     */
    @Valid
    private Q query;
}
