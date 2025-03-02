package org.isite.commons.cloud.converter;

import org.isite.commons.cloud.data.dto.ListRequest;
import org.isite.jpa.data.ListQuery;
import org.isite.jpa.data.Model;
import org.isite.jpa.data.Order;

import java.util.function.Supplier;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class ListQueryConverter {

    private ListQueryConverter() {
    }

    public static <Q, P extends Model<?>> ListQuery<P> toListQuery(ListRequest<Q> request, Supplier<P> constructor) {
        ListQuery<P> listQuery = new ListQuery<>(
                new Order(request.getOrder().getField(), request.getOrder().getDirection()));
        listQuery.setIndex(request.getIndex());
        listQuery.setPageSize(request.getPageSize());
        listQuery.setPo(DataConverter.convert(request.getQuery(), constructor));
        return listQuery;
    }
}
