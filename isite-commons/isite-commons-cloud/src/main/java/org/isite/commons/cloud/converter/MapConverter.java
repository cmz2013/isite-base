package org.isite.commons.cloud.converter;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class MapConverter {

    private MapConverter() {
    }

    /**
     * 转为Map
     * @param <K> Map#KEY
     * @param <V> Map#VALUE
     * @param srcList 源数据列表
     */
    public static <K, V> Map<K, V> toMap(Function<V, K> getter, List<V> srcList) {
        return CollectionUtils.isEmpty(srcList) ?
                Collections.emptyMap() : srcList.stream().collect(Collectors.toMap(getter, t -> t));
    }

    /**
     * 分组
     * @param <K> Map#KEY
     * @param <V> Map#VALUE(List<V>)
     * @param srcList 源数据列表
     */
    public static <K, V> Map<K, List<V>> groupBy(Function<V, K> getter, List<V> srcList) {
        return srcList.stream().collect(Collectors.groupingBy(getter));
    }
}
