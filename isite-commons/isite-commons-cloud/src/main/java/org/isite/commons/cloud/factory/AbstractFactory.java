package org.isite.commons.cloud.factory;

import org.isite.commons.lang.enums.Enumerable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.EnumMap;

import static org.apache.commons.lang.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Reflection.getGenericParameters;
import static org.isite.commons.lang.enums.Enumerable.getByCode;
import static org.isite.commons.lang.utils.TypeUtils.cast;

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
        E constant = getByCode(cast(getGenericParameter(this.getClass(), AbstractFactory.class, ONE)), code);
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
        Class<?>[] classes = getGenericParameters(this.getClass(), AbstractFactory.class);
        Class<S> strategyClass = cast(classes[ZERO]);
        Class<E> identityClass = cast(classes[ONE]);
        strategies = new EnumMap<>(identityClass);
        context.getBeansOfType(strategyClass).values().forEach(strategy -> {
            if (isNotEmpty(strategy.getIdentities())) {
                for (E identity : strategy.getIdentities()) {
                    strategies.put(identity, strategy);
                }
            }
        });
    }
}
