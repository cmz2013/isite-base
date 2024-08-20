package org.isite.commons.lang.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.commons.lang.enums.EnumerableSerializer;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 在 JSON 中，值必须是以下数据类型之一：字符串，数字，对象（JSON 对象），数组，布尔，NULL
 * @author zhangcm
 */
public class BeanSerializer extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config, BeanDescription description, List<BeanPropertyWriter> writers) {
        writers.forEach(writer -> {
            Class<?> clazz = writer.getType().getRawClass();
            if (Enumerable.class.isAssignableFrom(clazz)) {
                writer.assignSerializer(new EnumerableSerializer());
            } else if (isArray(clazz)) { //注册null Serializer
                writer.assignNullSerializer(new NullSerializer.NullArrayJsonSerializer());
            } else if (isString(clazz)) {
                writer.assignNullSerializer(new NullSerializer.NullStringJsonSerializer());
            }
        });
        return writers;
    }

    /**
     * 数组类型
     */
    private boolean isArray(Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 字符串类型
     */
    private boolean isString(Class<?> clazz) {
        return CharSequence.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz);
    }
}
