package org.isite.jpa.converter;

import org.isite.jpa.data.Page;
import org.isite.jpa.data.PageQuery;

import java.util.List;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PageConverter {

    private PageConverter() {
    }

    public static <T> Page<T> toPage(PageQuery<?> pageQuery, List<T> result, long total) {
        Page<T> page = new Page<>();
        page.setPageNum(pageQuery.getPageNum());
        page.setPageSize(pageQuery.getPageSize());
        page.addAll(result);
        page.setTotal(total);
        return page;
    }
}
