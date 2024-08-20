package org.isite.operation.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.operation.data.enums.EventType;

import javax.validation.constraints.NotNull;

/**
 * @Description 运营任务行为事件 DTO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class EventDto {
    /**
     * 用户id
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
     * 行为参数，在消息DTO中只负责传输，不需要也不知道解析成什么数据结构；在具体的任务接口中，根据定义好的VO进行数据解析（生产者和消费者有明确的数据结构）
     */
    private Object eventParam;
}