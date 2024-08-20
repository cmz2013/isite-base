package org.isite.jpa.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Page<T> extends ArrayList<T> {
    /**
     * 当前页数
     */
    private int pageNum;
    /**
     * 每页数据条数
     */
    private int pageSize;
    /**
     * 总条数。根据total计算总页数，可在用户端处理，减轻服务端负载
     */
    private long total;

    /**
     * 当前页数据
     */
    public List<T> getResult() {
        return this;
    }

    public static <T> Page<T> of(PageQuery<?> pageQuery, List<T> results, long total) {
        Page<T> page = new Page<>();
        page.setPageNum(pageQuery.getPageNum());
        page.setPageSize(pageQuery.getPageSize());
        page.addAll(results);
        page.setTotal(total);
        return page;
    }
}
