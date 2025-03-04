package org.isite.commons.lang.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.Constants;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class StringUtils {

    private StringUtils() {
    }

    public static String join(CharSequence delimiter, Object... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = Constants.ZERO; i < elements.length - Constants.ONE; i++) {
            builder.append(elements[i]);
            builder.append(delimiter);
        }
        builder.append(elements[elements.length - Constants.ONE]);
        return builder.toString();
    }
}
