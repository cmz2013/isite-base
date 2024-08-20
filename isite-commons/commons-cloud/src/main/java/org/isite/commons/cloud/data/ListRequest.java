package org.isite.commons.cloud.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.TEN;
import static org.isite.commons.lang.data.Constants.THOUSAND;
import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Description 根据有序的索引字段执行分页查询，不统计总条数和页数
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ListRequest<Q> {
    /**
     * 有序的索引字段，在当前页的最小值
     */
    @NotNull
    private Object minIndex;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 查询条件
     */
    @Valid
    private Q query;

    public ListRequest() {
        this.pageSize = TEN;
    }

    public void setPageSize(int pageSize) {
        isTrue(pageSize > ZERO && pageSize <= THOUSAND,
                "pageSize must be greater than 0 and less than or equal to 1000");
        this.pageSize = pageSize;
    }
}
