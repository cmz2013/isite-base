package org.isite.commons.cloud.converter;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @Description 数据转换器，用于防腐层转换模型
 * 1、只拷贝属性类型和属性名都相同的属性
 * 2、对应的属性要有getter和setter方法
 * @Author <font color='blue'>zhangcm</font>
 */
public class DataConverter {

    private DataConverter() {
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
        D target = constructor.get();
        if (null != source && null != target) {
            copyProperties(source, target, ignoreProperties);
        }
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
}
