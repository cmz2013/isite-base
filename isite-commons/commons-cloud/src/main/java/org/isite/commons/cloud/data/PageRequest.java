package org.isite.commons.cloud.data;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Order;

import javax.validation.Valid;
import java.util.List;

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.ONE;
import static org.isite.commons.lang.data.Constants.TEN;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * 分页查询请求参数
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PageRequest<Q> {
    /**
     * 当前页数，从1开始
     */
    private int pageNum;
    /**
     * 每页条数
     */
    private int pageSize;
    /**
     * @Description sql即可按单个字段排序，也可多个字段排序，多字段排序，优先级高的放前面，优先级低的放后面
     * JQuery Ajax传递PageRequest对象:
     * {pageSize:10,pageNum:1,orders:[{property:'create_time', direction:'DESC'}]}
     * JQuery Ajax会将orders数组参数映射成这样：
     * orders[0][property]=create_time
     * orders[0][direction]=DESC
     *
     * 而Spring MVC需要这种的参数格式：
     * orders[0].property=create_time
     * orders[0].direction=DESC
     *
     * 所以，JQuery Ajax使用以下方式传递orders：
     * {pageSize:10,pageNum:1,'orders[0].property':'create_time', 'orders[0].direction':'DESC'}
     */
    private List<Order> orders;
    /**
     * 查询条件
     */
    @Valid
    private Q query;

    public PageRequest() {
        this.pageNum = ONE;
        this.pageSize = TEN;
    }

    public void setPageSize(int pageSize) {
        isTrue(pageSize > ZERO && pageSize <= THOUSAND,
                "pageSize must be greater than 0 and less than or equal to 1000");
        this.pageSize = pageSize;
    }

    public void setPageNum(int pageNum) {
        isTrue(pageNum > ZERO, "pageNum must be greater than 0");
        this.pageNum = pageNum;
    }
}
