package org.isite.commons.lang.json;

import java.util.Collection;
import java.util.Date;

/**
 * JSON字段类型
 * @author <font color='blue'>zhangcm</font>
 */
public enum Type {
    /**
     * String
     */
    STRING,
    /**
     * Number
     */
    NUMBER,
    /**
     * Boolean
     */
    BOOLEAN,
    /**
     * Array
     */
    ARRAY,
    /**
     * Object
     */
    OBJECT;

    /**
     * 获取当前字段的JSON数据类型
     */
    public static Type getType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return NUMBER;
        }
        if (CharSequence.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz)) {
            return STRING;
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return BOOLEAN;
        }
        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            return ARRAY;
        }
        return OBJECT;
    }
}
