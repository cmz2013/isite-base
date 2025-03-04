package org.isite.commons.lang;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.enums.ResultStatus;

import java.util.Collection;
import java.util.Map;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class Assert {

    private Assert() {
    }

    public static void isNull(Object object, String message) {
        if (null != object) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notNull(Object object, String message) {
        if (null == object) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notNull(Object object, RuntimeException exception) {
        if (null == object) {
            throw exception;
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notEmpty(Collection<?> collection, RuntimeException exception) {
        if (CollectionUtils.isEmpty(collection)) {
            throw exception;
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void isTrue(Boolean expression, String message) {
        isTrue(expression, new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message));
    }

    public static void isTrue(Boolean expression, RuntimeException exception) {
        if (BooleanUtils.isNotTrue(expression)) {
            throw exception;
        }
    }

    public static void isTrue(Boolean expression, Runnable runnable) {
        if (BooleanUtils.isNotTrue(expression)) {
            runnable.run();
        }
    }

    public static void isFalse(Boolean expression, String message) {
        if (BooleanUtils.isNotFalse(expression)) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notEmpty(Object[] constants, String message) {
        if (ArrayUtils.isEmpty(constants)) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }

    public static void notBlank(String expression, String message) {
        if (StringUtils.isBlank(expression)) {
            throw new Error(ResultStatus.EXPECTATION_FAILED.getCode(), message);
        }
    }
}
