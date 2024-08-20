package org.isite.commons.cloud.factory;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 策略模式接口，该接口实例自动注册到工厂
 * @Author <font color='blue'>zhangcm</font>
 */
public interface Strategy <E extends Enum<E> & Enumerable<?>> {

    /**
     * @Description 获取策略标识。 如果返回空数组，该实例不会注册到工厂类中
     */
    E[] getIdentities();
}
