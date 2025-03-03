package org.isite.jpa.data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;

import java.util.List;
import java.util.stream.Collectors;

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
    private int pageNum = Constants.ONE;
    /**
     * 每页条数
     */
    private int pageSize = Constants.TEN;
    /**
     * 排序
     */
    private List<Order> orders;

    public int getOffset() {
        return (this.pageNum - Constants.ONE) * this.pageSize;
    }

    public String orderBy() {
        return CollectionUtils.isEmpty(orders) ? null :
                orders.stream().map(Order::orderBy).collect(Collectors.joining(Constants.COMMA));
    }

    public void setPageSize(int pageSize) {
        Assert.isTrue(pageSize > Constants.ZERO && pageSize <= Constants.THOUSAND,
                "pageSize is greater than 0 and less than or equal to 1000");
        this.pageSize = pageSize;
    }

    public void setPageNum(int pageNum) {
        Assert.isTrue(pageNum > Constants.ZERO, "pageNum is greater than 0");
        this.pageNum = pageNum;
    }
}
