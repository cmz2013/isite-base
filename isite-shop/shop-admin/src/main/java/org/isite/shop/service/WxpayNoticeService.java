package org.isite.shop.service;

import org.springframework.stereotype.Service;

import java.util.Map;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.isite.commons.lang.data.Constants.AMPERSAND;
import static org.isite.commons.lang.data.Constants.EQUALS_SIGN;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_KEY;
import static org.isite.shop.support.constants.WxpayConstants.FIELD_SIGN;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class WxpayNoticeService {

    public boolean isValidSignature(Map<String, String> params, String key) {
        String sign = params.get(FIELD_SIGN);
        String localSign = generateSignature(params, key);
        return sign.equals(localSign);
    }

    private String generateSignature(Map<String, String> params, String key) {
        StringBuilder plaintext = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().equals(FIELD_SIGN) || entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            plaintext.append(entry.getKey()).append(EQUALS_SIGN).append(entry.getValue()).append(AMPERSAND);
        }
        plaintext.append(FIELD_KEY).append(EQUALS_SIGN).append(key);
        return md5Hex(plaintext.toString()).toUpperCase();
    }
}
