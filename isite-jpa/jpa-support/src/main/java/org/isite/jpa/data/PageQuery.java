package org.isite.jpa.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.COMMA;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.TEN;
import static org.isite.commons.lang.Constants.THOUSAND;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PageQuery<P extends Model<?>> {
    /**
     * 查询条件
     */
    private P po;
    /**
     * 当前页数，从1开始
     */
    private int pageNum = ONE;
    /**
     * 每页条数
     */
    private int pageSize = TEN;
    /**
     * 排序
     */
    private List<Order> orders;

    public int getOffset() {
        return (this.pageNum - ONE) * this.pageSize;
    }

    public String orderBy() {
        return isEmpty(orders) ? null : orders.stream().map(Order::orderBy).collect(joining(COMMA));
    }

    public void setPageSize(int pageSize) {
        isTrue(pageSize > ZERO && pageSize <= THOUSAND,
                "pageSize is greater than 0 and less than or equal to 1000");
        this.pageSize = pageSize;
    }

    public void setPageNum(int pageNum) {
        isTrue(pageNum > ZERO, "pageNum is greater than 0");
        this.pageNum = pageNum;
    }
}
