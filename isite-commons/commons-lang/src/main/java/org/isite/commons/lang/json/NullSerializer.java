package org.isite.commons.lang.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static org.isite.commons.lang.data.Constants.BLANK_STRING;

/**
 * 自定义null值序列化处理器
 * @author zhangcm
 */
public class NullSerializer {

    private NullSerializer() {
    }

    /**
     * 数组集合，当值为null时，序列化成[]
     */
    public static class NullArrayJsonSerializer extends JsonSerializer<Object> {

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
    public static class NullStringJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, 
        					  SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(BLANK_STRING);
        }
    }
}
