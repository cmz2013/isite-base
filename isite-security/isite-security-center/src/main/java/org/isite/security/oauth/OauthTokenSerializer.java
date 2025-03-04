package org.isite.security.oauth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.enums.ResultStatus;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * @Description 自定义接口 /oauth/token 返回的数据
 * /oauth/token只用于返回token信息，不返回用户详情(/oauth/user)：保持接口单一职责
 * @Author <font color='blue'>zhangcm</font>
 */
public class OauthTokenSerializer extends StdSerializer<OauthToken> {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String TOKEN_TYPE = "tokenType";
    private static final String EXPIRES_IN = "expiresIn";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String SCOPE = "scope";

    public OauthTokenSerializer() {
        super(OauthToken.class);
    }

    @Override
    public void serialize(OauthToken token, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        generator.writeStartObject();
        generator.writeNumberField(Constants.RESULT_CODE, ResultStatus.OK.getCode());
        generator.writeStringField(Constants.RESULT_MESSAGE, ResultStatus.OK.getReasonPhrase());

        Map<String, Object> data = new HashMap<>();
        data.put(ACCESS_TOKEN, token.getValue());
        data.put(TOKEN_TYPE, token.getTokenType());
        OAuth2RefreshToken refreshToken = token.getRefreshToken();
        if (refreshToken != null) {
            data.put(REFRESH_TOKEN, refreshToken.getValue());
        }
        Date expiration = token.getExpiration();
        if (expiration != null) {
            data.put(EXPIRES_IN, (expiration.getTime() - System.currentTimeMillis()) / ChronoUnit.SECOND.getMillis());
        }
        Set<String> scopeSet = token.getScope();
        if (CollectionUtils.isNotEmpty(scopeSet)) {
            StringBuilder scopes = new StringBuilder();
            scopeSet.forEach(scope -> scopes.append(scope).append(Constants.SPACE));
            data.put(SCOPE, scopes.substring(Constants.ZERO, scopes.length() - Constants.ONE));
        }
        generator.writeObjectField(Constants.RESULT_DATA, data);
        generator.writeEndObject();
    }
}
