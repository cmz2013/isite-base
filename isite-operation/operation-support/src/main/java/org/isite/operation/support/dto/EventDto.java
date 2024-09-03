package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.operation.support.enums.EventType;

import javax.validation.constraints.NotNull;

/**
 * @Description 运营任务事件 DTO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class EventDto {
    /**
     * 当前登录用户id
     */
    @NotNull
    private Long userId;
    /**
     * @Description 行为类型（和REST接口一一对应）
     * 一个行为类型可以对应多种运营任务类型。业务接口根据行为类型发送运营任务消息，
     * 而不是根据任务类型发送，这么做可以降低业务接口和运营任务活的得耦合，更方便扩展
     */
    @NotNull
    private EventType eventType;
    /**
     * 对象值，对应于EventType#objectType
     */
    private String objectValue;
    /**
     * @Description 行为参数（生产者和消费者有明确的数据结构）。
     * 在EventConsumer执行任务之前，需要根据行为类型进行解析和转换行为参数。
     */
    private Object eventParam;

}