package org.isite.commons.cloud.data;

import org.isite.commons.lang.enums.EnumConstantNotFound;
import org.isite.commons.lang.enums.Enumerable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @Description 自定义通用的枚举（要继承Enumerable）Converter，根据名称或编码获取枚举常量
 * @Author <font color='blue'>zhangcm</font>
 */
public class EnumConverter implements ConverterFactory<String, Enumerable<?>> {

    @Override
    public <E extends Enumerable<?>> Converter<String, E> getConverter(Class<E> targetType) {
        return value -> {
            if (isBlank(value)) {
                return null;
            }
            for (E constant : targetType.getEnumConstants()) {
                if (((Enum<?>) constant).name().equals(value) || constant.getCode().toString().equals(value)) {
                    return constant;
                }
            }
            throw new EnumConstantNotFound(targetType, value);
        };
    }
}
