package org.isite.jpa.data;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;

/**
 * @Description 根据有序索引执行分页查询的PO参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class ListQuery<P extends Model<?>> {
    /**
     * 查询条件
     */
    @Setter
    private P po;
    /**
     * 每页条数
     */
    private Integer pageSize = Constants.TEN;
    /**
     * 在当前页的最小值或最大值，取决于排序方式。index为null时，查询第一页数据
     */
    @Setter
    private Object index;
    /**
     * 有序索引字段排序
     */
    private final Order order;

    public ListQuery(Order order) {
        Assert.notNull(order, "order cannot be null");
        this.order = order;
    }

    public void setPageSize(int pageSize) {
        Assert.isTrue(pageSize > Constants.ZERO && pageSize <= Constants.THOUSAND,
                "pageSize is greater than 0 and less than or equal to 1000");
        this.pageSize = pageSize;
    }
}
