package org.isite.commons.lang;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.BooleanUtils.isNotFalse;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.isite.commons.lang.enums.ResultStatus.EXPECTATION_FAILED;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class Assert {

    private Assert() {
    }

    public static void isNull(Object object, String message) {
        if (null != object) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notNull(Object object, String message) {
        if (null == object) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notNull(Object object, RuntimeException exception) {
        if (null == object) {
            throw exception;
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notEmpty(Collection<?> collection, RuntimeException exception) {
        if (CollectionUtils.isEmpty(collection)) {
            throw exception;
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (isNotEmpty(collection)) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void isTrue(Boolean expression, String message) {
        isTrue(expression, new Error(EXPECTATION_FAILED.getCode(), message));
    }

    public static void isTrue(Boolean expression, RuntimeException exception) {
        if (isNotTrue(expression)) {
            throw exception;
        }
    }

    public static void isTrue(Boolean expression, Runnable runnable) {
        if (isNotTrue(expression)) {
            runnable.run();
        }
    }

    public static void isFalse(Boolean expression, String message) {
        if (isNotFalse(expression)) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notEmpty(Object[] constants, String message) {
        if (ArrayUtils.isEmpty(constants)) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notBlank(String expression, String message) {
        if (isBlank(expression)) {
            throw new Error(EXPECTATION_FAILED.getCode(), message);
        }
    }
}
