package org.isite.commons.lang.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;

import java.lang.reflect.Array;
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
        Assert.isTrue(object.getClass().isArray(), object.getClass().getName() + " is not array");
        T[] objects = cast(Array.newInstance(clazz, Array.getLength(object)));
        if (ArrayUtils.isNotEmpty(objects)) {
            for (int i = Constants.ZERO; i < objects.length; i++) {
                objects[i] = cast(Array.get(object, i));
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
        return (T) object;
    }
}
