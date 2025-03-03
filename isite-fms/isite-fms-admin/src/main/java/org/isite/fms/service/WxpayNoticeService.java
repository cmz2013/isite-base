package org.isite.fms.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.isite.commons.lang.Constants;
import org.isite.fms.data.constants.WxpayConstants;
import org.springframework.stereotype.Service;

import java.util.Map;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Service
public class WxpayNoticeService {

    public boolean isValidSignature(Map<String, String> params, String key) {
        String sign = params.get(WxpayConstants.FIELD_SIGN);
        String localSign = generateSignature(params, key);
        return sign.equals(localSign);
    }

    private String generateSignature(Map<String, String> params, String key) {
        StringBuilder plaintext = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().equals(WxpayConstants.FIELD_SIGN) || entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            plaintext.append(entry.getKey()).append(Constants.EQUAL_SIGN).append(entry.getValue()).append(Constants.AMPERSAND);
        }
        plaintext.append(WxpayConstants.FIELD_KEY).append(Constants.EQUAL_SIGN).append(key);
        return DigestUtils.md5Hex(plaintext.toString()).toUpperCase();
    }
}
