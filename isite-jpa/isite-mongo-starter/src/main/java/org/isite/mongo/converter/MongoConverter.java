package org.isite.mongo.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * @Description 如果MongoDB存储的不是枚举常量name，必须定义该枚举的Converter Bean
 * @param <M> Mongo读取返回的数据类
 * @param <V> Mongo存储写入的数据类
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class MongoConverter<M, V> {
    /**
     * 返回读和写写操作的Converter
     */
    public Converter<?, ?>[] conversions() {
        return new Converter[] {createWritingConverter(), createReadingConverter()};
    }

    /**
     * 创建 ReadingConverter
     */
    protected abstract Converter<V, M> createReadingConverter();

    /**
     * 创建 WritingConverter
     */
    protected abstract Converter<M, V> createWritingConverter();
}
