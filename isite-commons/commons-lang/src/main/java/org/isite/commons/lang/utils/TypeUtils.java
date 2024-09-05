package org.isite.commons.lang.utils;

import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class TypeUtils {

    private TypeUtils() {
    }

    /**
     * 基本数据类型
     */
    public static boolean isBasic(Object data) {
        if (null == data) {
            return false;
        }
        return data instanceof CharSequence || data instanceof Number ||
                data instanceof Boolean || data instanceof Character;
    }

    /**
     * @Description 将代表一个数组对象的object强转为数组
     * 注意：如果object对象是int等基本类型的数组，不能直接强转: (T[]) object
     */
    public static <T> T[] castArray(Object object, Class<T> clazz) {
        if (null == object) {
            return null;
        }
        isTrue(object.getClass().isArray(), object.getClass().getName() + " is not array");
        T[] objects = cast(newInstance(clazz, getLength(object)));
        if (isNotEmpty(objects)) {
            for (int i = ZERO; i < objects.length; i++) {
                objects[i] = cast(get(object, i));
            }
        }
        return objects;
    }

    /**
     * @Description 类型强转
     * 告诉编译器忽略泛型强转未经检查的警告：@SuppressWarnings("unchecked")
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        if (null == object) {
            return null;
        }
        return (T) object;
    }
}
