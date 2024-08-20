package org.isite.security.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.data.Constants.RESULT_CODE;
import static org.isite.commons.lang.data.Constants.RESULT_MESSAGE;
import static org.springframework.http.HttpStatus.PROXY_AUTHENTICATION_REQUIRED;

/**
 * @description AuthenticationException的序列化实现
 * @author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class AuthenticationExceptionSerializer extends StdSerializer<AuthenticationException> {

    public AuthenticationExceptionSerializer() {
        super(AuthenticationException.class);
    }

    @Override
    public void serialize(AuthenticationException e,
                          JsonGenerator generator, SerializerProvider provider) throws IOException {
        log.error(e.getMessage(), e);
        generator.writeStartObject();
        generator.writeNumberField(RESULT_CODE, PROXY_AUTHENTICATION_REQUIRED.value());
        generator.writeStringField(RESULT_MESSAGE, getMessage(e));
        generator.writeEndObject();
    }
}
