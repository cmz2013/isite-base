package org.isite.commons.lang.enums;

/**
 * 未找到枚举常量
 *
 * @author <font color='blue'>zhangcm</font>
 */
public class EnumConstantNotFound extends RuntimeException {

    public EnumConstantNotFound(Class<?> enumType, Object value) {
        super(enumType.getName() + "#" + value);
    }
}
