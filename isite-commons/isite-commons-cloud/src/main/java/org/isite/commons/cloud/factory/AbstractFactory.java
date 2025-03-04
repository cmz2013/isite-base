package org.isite.commons.cloud.factory;

import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.commons.lang.utils.TypeUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.EnumMap;
/**
 * @Description 抽象工厂
 * @param <S> 策略接口，接口子类自动注册到工厂类中
 * @param <E> 实现了Enumerable接口的枚举类
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class AbstractFactory<S extends Strategy<E>, E extends Enum<E> & Enumerable<T>, T>
        implements ApplicationContextAware {

    private EnumMap<E, S> strategies;

    /**
     * 根据枚举code返回Bean
     */
    public S get(T code) {
        E constant = Enumerable.getByCode(TypeUtils.cast(
                Reflection.getGenericParameter(this.getClass(), AbstractFactory.class, Constants.ONE)), code);
        return null == constant? null : get(constant);
    }

    /**
     * 根据枚举常量返回Bean
     */
    public S get(E constant) {
        return strategies.get(constant);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Class<?>[] classes = Reflection.getGenericParameters(this.getClass(), AbstractFactory.class);
        Class<S> strategyClass = TypeUtils.cast(classes[Constants.ZERO]);
        Class<E> identityClass = TypeUtils.cast(classes[Constants.ONE]);
        strategies = new EnumMap<>(identityClass);
        context.getBeansOfType(strategyClass).values().forEach(strategy -> {
            if (ArrayUtils.isNotEmpty(strategy.getIdentities())) {
                for (E identity : strategy.getIdentities()) {
                    strategies.put(identity, strategy);
                }
            }
        });
    }
}
