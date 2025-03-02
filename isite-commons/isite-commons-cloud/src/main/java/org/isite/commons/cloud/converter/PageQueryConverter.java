package org.isite.commons.cloud.converter;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.jpa.data.Model;
import org.isite.jpa.data.Order;
import org.isite.jpa.data.PageQuery;

import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class PageQueryConverter {

    private PageQueryConverter() {
    }

    public static <Q, P extends Model<?>> PageQuery<P> toPageQuery(PageRequest<Q> request, Supplier<P> constructor) {
        PageQuery<P> pageQuery = new PageQuery<>();
        pageQuery.setPageNum(request.getPageNum());
        pageQuery.setPageSize(request.getPageSize());
        pageQuery.setPo(DataConverter.convert(request.getQuery(), constructor));
        if (CollectionUtils.isNotEmpty(request.getOrders())) {
            pageQuery.setOrders(request.getOrders().stream().map(
                    dto -> new Order(dto.getField(), dto.getDirection())).collect(Collectors.toList()));
        }
        return pageQuery;
    }
}
