package org.isite.commons.cloud.converter;

import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.Enumerable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @Description 自定义枚举Converter，根据名称或value或Enumerable编码获取枚举常量
 * @Author <font color='blue'>zhangcm</font>
 */
public class EnumConstantConverter implements ConverterFactory<Object, Enum<?>> {

    @Override
    public <E extends Enum<?>> Converter<Object, E> getConverter(Class<E> targetType) {
        return value -> {
            if (Constants.BLANK_STR.equals(value)) {
                return null;
            }
            for (E constant : targetType.getEnumConstants()) {
                if (constant.equals(value) || constant.name().equals(value)) {
                    return constant;
                }
                if (constant instanceof Enumerable && ((Enumerable) constant).getCode().equals(value)) {
                    return constant;
                }
            }
            throw new EnumConstantNotPresentException(targetType, value.toString());
        };
    }
}
