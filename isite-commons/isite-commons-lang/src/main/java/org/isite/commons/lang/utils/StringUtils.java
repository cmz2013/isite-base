package org.isite.commons.lang.utils;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class StringUtils {

    private StringUtils() {
    }

    public static String join(CharSequence delimiter, Object... elements) {
        if (isEmpty(elements)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = ZERO; i < elements.length - ONE; i++) {
            builder.append(elements[i]);
            builder.append(delimiter);
        }
        builder.append(elements[elements.length - ONE]);
        return builder.toString();
    }
}
