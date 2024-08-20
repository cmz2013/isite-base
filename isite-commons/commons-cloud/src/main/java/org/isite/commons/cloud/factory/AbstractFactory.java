package org.isite.commons.cloud.factory;

import org.isite.commons.lang.enums.Enumerable;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.lang.Reflection.getGenericParameters;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;

/**
 * @Description 抽象工厂
 * @param <S> 策略接口，接口子类自动注册到工厂类中
 * @param <E> 实现了Enumerable接口的枚举类
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class AbstractFactory<S extends Strategy<E>, E extends Enum<E> & Enumerable<T>, T>
        implements ApplicationContextAware {

    private List<S> strategies;

    /**
     * 根据枚举code返回Bean
     */
    public S get(T code) {
        for (S strategy : strategies) {
            for (E identity : strategy.getIdentities()) {
                if (identity.getCode().equals(code)) {
                    return strategy;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举常量返回Bean
     */
    public S get(E constant) {
        return get(constant.getCode());
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Class<?>[] classes = getGenericParameters(this.getClass(), AbstractFactory.class);
        Class<S> strategyClass = cast(classes[ZERO]);
        strategies = new ArrayList<>();
        context.getBeansOfType(strategyClass).values().forEach(strategy -> {
            if (isNotEmpty(strategy.getIdentities())) {
                strategies.add(strategy);
            }
        });
    }
}
