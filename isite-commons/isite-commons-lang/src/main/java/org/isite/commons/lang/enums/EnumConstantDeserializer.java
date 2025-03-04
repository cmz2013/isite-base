package org.isite.commons.lang.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.utils.TypeUtils;

import java.io.IOException;
import java.io.Serializable;
/**
 * @Description 枚举类型JSON反序列化通用实现（枚举类要实现Enumerable接口）
 * 如果JSON数据不是枚举常量name，必须定义该枚举的反序列化实现
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class EnumConstantDeserializer<E extends Enum<?> & Enumerable<?>>
        extends JsonDeserializer<E> implements Serializable {

    @Override
    public E deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        Class<E> eClass = TypeUtils.cast(Reflection.getGenericParameter(this.getClass(), EnumConstantDeserializer.class));
        String value = parser.getCodec().readValue(parser, String.class);
        for (E constant : eClass.getEnumConstants()) {
            //根据名称或编码获取枚举常量
            if (constant.name().equals(value) || constant.getCode().toString().equals(value)) {
                return constant;
            }
        }
        throw new EnumConstantNotPresentException(eClass, value);
    }
}
