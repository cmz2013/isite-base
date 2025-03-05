package org.isite.commons.lang.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.isite.commons.lang.Constants;

import java.io.IOException;
/**
 * @Description 自定义null值序列化处理器
 * @Author zhangcm
 */
public class NullSerializer {

    private NullSerializer() {
    }

    /**
     * 数组集合，当值为null时，序列化成[]
     */
    public static class NullArraySerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator,
        					  SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeEndArray();
        }
    }

    /**
     * 字符串，当值为null时，序列化成""
     */
    public static class NullStringSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, 
        					  SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(Constants.BLANK_STR);
        }
    }
}
