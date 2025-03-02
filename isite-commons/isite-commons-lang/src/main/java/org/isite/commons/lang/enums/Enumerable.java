package org.isite.commons.lang.enums;

import org.isite.commons.lang.Assert;

import javax.annotation.Nullable;

/**
 * @Description 枚举类父接口，规范枚举的实现
 * @param <T> 枚举编码类型
 * 枚举类也是类，也可以有自己的成员变量，成员方法，静态方法、静态变量等，也能实现其他的接口。
 * 枚举类与普通类的不同在于：
 * 1、枚举类可以实现接口，但不能继承其他类了（因为已经继承了java.lang.Enum）
 * 2、枚举类的构造器私有
 * 3、枚举类不可以被其它的外部类继承。若果其它的外部类继承枚举类，由于在构造类的对象时，
 *    需要调用父类的构造方法，由于枚举类的构造器私有，所以无法调用。
 *    但是，枚举类可以被内部类继承。内部类能访问外部类的任何成员，当然能访问已被私有的构造器了。
 * @Author <font color='blue'>zhangcm</font>
 */
public interface Enumerable<T> {
    /**
     * @Description 枚举常量编码，一般用于数据库存储。不为空
     * @return 编码
     */
    T getCode();

    /**
     * @Description 获取枚举常量。static方法不能被子类重写
     * @param eClass 枚举类
     * @param code 编码
     */
    static <E extends Enumerable<?>> @Nullable E getByCode(Class<E> eClass, Object code) {
        Assert.isTrue(eClass.isEnum(), eClass.getName() + " is not an enum");
        // getEnumConstants()方法用于返回枚举常量数组,当此类对象不表示枚举类型时，它返回null
        for (E constant : eClass.getEnumConstants()) {
            if (constant.getCode().equals(code)) {
                return constant;
            }
        }
        return null;
    }

    /**
     * @Description 获取枚举常量。static方法不能被子类重写
     * @param eClass 枚举类
     * @param name 常量名
     */
    static <E extends Enum<?>> @Nullable E getByName(Class<E> eClass, String name) {
        for (E constant : eClass.getEnumConstants()) {
            if (constant.name().equals(name)) {
                return constant;
            }
        }
        return null;
    }
}