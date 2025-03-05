package org.isite.commons.lang.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class EnumerableSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object object, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(((Enumerable<?>) object).getCode());
    }
}
