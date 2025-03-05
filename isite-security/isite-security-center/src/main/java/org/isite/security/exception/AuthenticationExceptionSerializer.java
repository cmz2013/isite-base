package org.isite.security.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Constants;
import org.springframework.http.HttpStatus;

import java.io.IOException;
/**
 * @Description AuthenticationException的序列化实现
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class AuthenticationExceptionSerializer extends StdSerializer<AuthenticationException> {

    public AuthenticationExceptionSerializer() {
        super(AuthenticationException.class);
    }

    @Override
    public void serialize(AuthenticationException e, JsonGenerator generator, SerializerProvider provider) throws IOException {
        log.error(e.getMessage(), e);
        generator.writeStartObject();
        generator.writeNumberField(Constants.RESULT_CODE, HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value());
        generator.writeStringField(Constants.RESULT_MESSAGE, MessageUtils.getMessage(e));
        generator.writeEndObject();
    }
}
