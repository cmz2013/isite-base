package org.isite.commons.cloud.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 分页查询请求参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PageRequest<Q> {
    /**
     * 当前页数
     */
    @NotNull
    private Integer pageNum;
    /**
     * 每页条数
     */
    @NotNull
    private Integer pageSize;
    /**
     * @Description sql即可按单个字段排序，也可多个字段排序，多字段排序，优先级高的放前面，优先级低的放后面
     * JQuery Ajax传递PageRequest对象:
     * {pageSize:10,pageNum:1,orders:[{property:'create_time', direction:'DESC'}]}
     * JQuery Ajax会将orders数组参数映射成这样：
     * orders[0][field]=create_time
     * orders[0][direction]=DESC
     *
     * 而Spring MVC需要这种的参数格式：
     * orders[0].field=create_time
     * orders[0].direction=DESC
     * 所以，JQuery Ajax使用以下方式传递orders：
     * {pageSize:10,pageNum:1,'orders[0].field':'create_time', 'orders[0].direction':'DESC'}
     */
    @Valid
    private List<OrderDto> orders;
    /**
     * 查询条件
     */
    @Valid
    private Q query;
}
