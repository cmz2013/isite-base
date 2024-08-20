package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 回答问题行为参数，用于解析JSON字符串：EventDto#eventParam
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ReplyEventParam {
    /**
     * 是否首答
     */
    private Boolean firstAnswer;
    /**
     * 响应时间（分钟）
     */
    private Integer responseTime;
}
