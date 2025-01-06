package org.isite.commons.cloud.converter;

import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.jpa.data.Model;
import org.isite.jpa.data.Order;
import org.isite.jpa.data.PageQuery;

import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.cloud.converter.DataConverter.convert;

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
        pageQuery.setPo(convert(request.getQuery(), constructor));
        if (isNotEmpty(request.getOrders())) {
            pageQuery.setOrders(request.getOrders().stream().map(
                    dto -> new Order(dto.getField(), dto.getDirection())).collect(toList()));
        }
        return pageQuery;
    }
}
