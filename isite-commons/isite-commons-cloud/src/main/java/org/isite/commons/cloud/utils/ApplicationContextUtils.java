package org.isite.commons.cloud.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description ApplicationContext工具类，Bean加载完以后才可以使用，不能在配置类中使用，否则会报错。
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
  /**
   * Spring Bean上下文
   */
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
    ApplicationContextUtils.applicationContext = applicationContext;
  }

  public static <T> T getBean(Class<T> t) {
    return applicationContext.getBean(t);
  }

  public static Object getBean(String name) {
    return applicationContext.getBean(name);
  }

  public static <T> Map<String, T> getBeans(Class<T> t) {
    return applicationContext.getBeansOfType(t);
  }

}