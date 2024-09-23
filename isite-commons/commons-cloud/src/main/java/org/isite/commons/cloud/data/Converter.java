package org.isite.commons.cloud.data;

import lombok.SneakyThrows;
import org.isite.commons.lang.Error;
import org.isite.jpa.data.ListQuery;
import org.isite.jpa.data.Model;
import org.isite.jpa.data.OrderQuery;
import org.isite.jpa.data.PageQuery;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.enums.ResultStatus.EXPECTATION_FAILED;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * 转换器，用于防腐层转换模型
 * 1、只拷贝属性类型和属性名都相同的属性
 * 2、对应的属性要有getter和setter方法
 * @author <font color='blue'>zhangcm</font>
 */
public class Converter {

    private Converter() {
    }

    /**
     * 数据转换
     * @param source 源数据
     * @param constructor 目标类构造函数
     * @param <S> 源数据类
     * @param <D> 目标数据类
     */
    @SneakyThrows
    public static <S, D> D convert(S source, Supplier<D> constructor, String... ignoreProperties) {
        if (null == source) {
            return null;
        }
        D target = constructor.get();
        copyProperties(source, target, ignoreProperties);
        return target;
    }

    /**
     * 数据转换
     * @param sources 源数据
     * @param constructor 目标类构造函数
     * @param <S> 源数据类
     * @param <D> 目标数据类
     */
    public static <S, D> List<D> convert(List<S> sources, Supplier<D> constructor, String... ignoreProperties) {
        return isEmpty(sources) ? emptyList() : sources.stream()
                .map(source -> convert(source, constructor, ignoreProperties)).collect(toList());
    }

    /**
     * 数据转换
     * @param srcList 源数据列表
     * @param getter getter方法
     */
    public static <T, I> List<I> convert(List<T> srcList, Function<T, I> getter) {
        return isEmpty(srcList) ? emptyList() : srcList.stream().map(getter).filter(Objects::nonNull)
                .distinct().collect(toList());
    }

    /**
     * 转为Map
     * @param srcList 源数据列表
     * @param <K> Map#KEY
     * @param <V> Map#VALUE
     */
    public static <K, V> Map<K, V> toMap(List<V> srcList, Function<V, K> getter) {
        return isEmpty(srcList) ? emptyMap() : srcList.stream().collect(Collectors.toMap(getter, t -> t));
    }

    /**
     * 分组
     * @param srcList 源数据列表
     * @param <K> Map#KEY
     * @param <V> Map#VALUE(List<V>)
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> srcList, Function<V, K> getter) {
        return srcList.stream().collect(groupingBy(getter));
    }

    public static <Q, P extends Model<?>> ListQuery<P> toListQuery(ListRequest<Q> request, Supplier<P> constructor) {
        ListQuery<P> listQuery = new ListQuery<>(
                new OrderQuery(request.getOrder().getField(), request.getOrder().getDirection()));
        listQuery.setIndex(request.getIndex());
        listQuery.setPageSize(request.getPageSize());
        listQuery.setPo(convert(request.getQuery(), constructor));
        return listQuery;
    }

    public static <Q, P extends Model<?>> PageQuery<P> toPageQuery(PageRequest<Q> request, Supplier<P> constructor) {
        PageQuery<P> pageQuery = new PageQuery<>();
        pageQuery.setPageNum(request.getPageNum());
        pageQuery.setPageSize(request.getPageSize());
        if (null != constructor) {
            pageQuery.setPo(convert(request.getQuery(), constructor));
        }
        if (isNotEmpty(request.getOrders())) {
            pageQuery.setOrders(request.getOrders().stream().map(
                    order -> new OrderQuery(order.getField(), order.getDirection())).collect(toList()));
        }
        return pageQuery;
    }

    public static Result<Object> toResult(Throwable throwable) {
        if (throwable instanceof Error) {
            Error error = (Error) throwable;
            return new Result<>(error.getCode(), getMessage(error));
        }
        if (throwable instanceof ResponseStatusException) {
            ResponseStatusException exception = (ResponseStatusException) throwable;
            return new Result<>(exception.getStatus().value(), exception.getMessage());
        }
        return new Result<>(EXPECTATION_FAILED.getCode(), getMessage(throwable));
    }
}
