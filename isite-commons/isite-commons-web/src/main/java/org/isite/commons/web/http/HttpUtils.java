package org.isite.commons.web.http;

import java.util.Map;

import static org.apache.commons.collections4.MapUtils.isEmpty;
import static org.isite.commons.lang.Constants.AMPERSAND;
import static org.isite.commons.lang.Constants.BLANK_STR;
import static org.isite.commons.lang.Constants.EQUAL_SIGN;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * HTTP工具类
 * @author <font color='blue'>zhangcm</font>
 */
public class HttpUtils {

    private HttpUtils() {
    }

    /**
     * 按照key1=val1&key2=val2 的方式进行编码
     */
    public static String toFormData(Map<String, Object> params) {
        if (isEmpty(params)) {
            return BLANK_STR;
        }
        StringBuilder results = new StringBuilder();
        /*
         * entrySet遍历map比keySet更快：
         * entrySet()首先遍历数组，再遍历数组每个元素后面对应的链表，这种方式其实就是实现hashMap的原理
         * keySet()首先遍历出所有的key，再次根据key去查找对应的值，而单链表的增删快，查找慢
         * values()只遍历value，效率最快，但实用性比较低
         */
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            results.append(entry.getKey());
            results.append(EQUAL_SIGN);
            results.append(entry.getValue());
            results.append(AMPERSAND);
        }
        return results.substring(ZERO, results.length() - ONE);
    }
}