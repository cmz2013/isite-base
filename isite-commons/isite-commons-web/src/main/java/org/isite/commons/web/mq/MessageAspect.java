package org.isite.commons.web.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
/**
 * @Description 通过AOP切面方式，使用方法入参和返回值构造和发送MQ消息，与业务代码解耦，实现代码复用。
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
@Aspect
@Component
@ConditionalOnBean(value = RabbitAutoConfiguration.class)
public class MessageAspect implements Ordered {

    /**
     * RabbitTemplate 提供了消息接收/发送等方法
     */
    private RabbitTemplate rabbitTemplate;

    @Pointcut("@annotation(org.isite.commons.web.mq.Publisher)")
    public void access() {
        //用于匹配持有@Publisher注解的方法
    }

    @Around("@annotation(publisher)")
    public Object doBefore(ProceedingJoinPoint point, Publisher publisher) throws Throwable {
        Object returnValue = point.proceed();
        Message[] messages = publisher.messages();
        if (ArrayUtils.isEmpty(messages)) {
            return returnValue;
        }
        for (Message message : messages) {
            sendMessage(message, point.getArgs(), returnValue);
        }
        return returnValue;
    }

    /**
     * 发送消息
     */
    private void sendMessage(Message message, Object[] args, Object returnValue) {
        try {
            Object body = ApplicationContextUtils.getBean(message.producer()).getBody(args, returnValue);
            if (null != body) {
                this.rabbitTemplate.convertAndSend(message.queues(), body);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public int getOrder() {
        return -300;
    }
}
