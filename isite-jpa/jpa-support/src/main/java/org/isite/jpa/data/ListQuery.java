package org.isite.jpa.data;

import lombok.Getter;
import lombok.Setter;

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.TEN;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Description 根据有序索引执行分页查询的PO参数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ListQuery {
    /**
     * 有序的索引字段，在当前页的最小值
     */
    private Object minIndex;
    /**
     * 每页数据条数
     */
    private Integer pageSize = TEN;

    public void setPageSize(int pageSize) {
        isTrue(pageSize > ZERO && pageSize <= THOUSAND,
                "pageSize is greater than 0 and less than or equal to 1000");
        this.pageSize = pageSize;
    }
}
