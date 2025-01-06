package org.isite.data.log;

import lombok.extern.slf4j.Slf4j;
import org.isite.data.support.dto.DataLogDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.isite.data.support.constants.DataConstants.QUEUE_DATA_LOG;

/**
 * @Description 数据接口日志处理
 * LogHandler不支持Serializable:
 * 1) ExecutorConfigurer（ExecutorAdapter） 配置类上使用了Enable注解，不支持Serializable
 * 2) spring-cloud-starter-alibaba-sentinel代理的feign客户端（DataClient）不支持Serializable
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Component
public class LogHandler {

    private LogStrategy logStrategy;
    /**
     * RabbitTemplate 提供了消息接收/发送等方法
     */
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setLogStrategy(LogStrategy logStrategy) {
        this.logStrategy = logStrategy;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * @Description 日志处理
     */
    public void handle(DataLogDto logDto) {
        try {
            //如果ID不为空，则为重试操作，重试接口同步返回最新日志
            if (isNotBlank(logDto.getId())  || this.logStrategy.discard(logDto)) {
                return;
            }
            rabbitTemplate.convertAndSend(QUEUE_DATA_LOG, logDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 保存日志，发送数据接口失败告警
     * @param logDto 接口日志
     * @param e 异常
     */
    public void handle(DataLogDto logDto, Throwable e) {
        log.error(e.getMessage(), e);
        logDto.setStatus(FALSE);
        logDto.setRemark(e.getClass().getName());
        handle(logDto);
    }
}
