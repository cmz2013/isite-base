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
     */
    @Bean
    @ConditionalOnMissingBean
    public LogStrategy getLogStorage() {
        return new ErrorStrategy();
    }
}
