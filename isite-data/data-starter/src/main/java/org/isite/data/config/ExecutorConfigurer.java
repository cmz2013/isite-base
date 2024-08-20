package org.isite.data.config;

import org.isite.data.log.ErrorStrategy;
import org.isite.data.log.LogStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 配置执行器
 * @Author <font color='blue'>zhangcm</font>
 */
@Configuration
public class ExecutorConfigurer {
    /**
     * @Description 数据接口日志快照默认策略。
     * 注意,不能在配置类的其他@Bean方法中调用getLogStorage()，因为该方法添加了@ConditionalOnMissingBean注解，所以可能没有实例化
     */
    @Bean
    @ConditionalOnMissingBean
    public LogStrategy getLogStorage() {
        return new ErrorStrategy();
    }
}
