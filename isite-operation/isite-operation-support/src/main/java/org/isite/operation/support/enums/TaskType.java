package org.isite.operation.support.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.operation.support.vo.AnswerPrizeProperty;
import org.isite.operation.support.vo.OrderPrizeProperty;
import org.isite.operation.support.vo.PrizeTaskProperty;
import org.isite.operation.support.vo.Reward;
import org.isite.operation.support.vo.ScoreTaskProperty;
import org.isite.operation.support.vo.SignScoreProperty;
import org.isite.operation.support.vo.TaskProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @Description 运营任务类型。
 * 配置运营活动时，先选择行为类型（EventType），再根据行为类型查询和选择任务类型（TaskType）
 * 使用 @JsonFormat 注解可以将枚举类转为json再返回给前端
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskType implements Enumerable<Integer> {
    /**
     * 积分类型任务
     */
    USER_SCORE(1, EventType.PUT_USER, "提交个人信息送积分", ScoreTaskProperty.class),
    OPERATION_SIGN_SCORE(2, EventType.POST_OPERATION_SIGN, "每日签到送积分", SignScoreProperty.class),

    /**
     * 奖品类型任务
     */
    QUESTION_PRIZE(101, EventType.POST_QUESTION, "提交问题送奖品", PrizeTaskProperty.class),
    QUESTION_REPLY_PRIZE(102, EventType.POST_QUESTION_REPLY, "答疑送奖品", AnswerPrizeProperty.class),
    /**
     * 提问人采纳答案时，给回复人送奖品。eventDto#userId为提问人ID，eventParam为回答人ID
     */
    QUESTION_REPLY_ADOPT_PRIZE(103, EventType.PUT_QUESTION_REPLY_ADOPT, "答案被采纳送奖品", AnswerPrizeProperty.class),
    /**
     * 老用户在活动期间完成消费，且在一年内累计消费金额满足条件时发放福利（eventParam为用户累计消费金额）
     */
    SHOP_ORDER_PRIZE(104, EventType.POST_SHOP_PAYMENT_NOTIFY, "老用户福利", OrderPrizeProperty.class),

    /**
     * 邀请类型任务（邀请码不为空），保存邀请记录再送奖励
     */
    QUESTION_REPLY_INVITE(201, EventType.POST_QUESTION_REPLY, "答疑邀请", PrizeTaskProperty.class),
    OPERATION_WEBPAGE_INVITE(202, EventType.GET_OPERATION_WEBPAGE, "活动邀请", PrizeTaskProperty.class),
    ;

    private final Integer code;
    private final EventType eventType;
    @Getter
    private final String label;
    private final Class<? extends TaskProperty<? extends Reward>> propertyClass;

    TaskType(Integer code, EventType eventType, String label,
             Class<? extends TaskProperty<? extends Reward>> propertyClass) {
        this.code = code;
        this.eventType = eventType;
        this.label = label;
        this.propertyClass = propertyClass;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @JsonIgnore
    public EventType getEventType() {
        return eventType;
    }

    @JsonIgnore
    public Class<? extends TaskProperty<? extends Reward>> getPropertyClass() {
        return propertyClass;
    }

    /**
     * @Description 当前行为类型可以触发的任务类型
     */
    public static List<TaskType> values(EventType event) {
        return Arrays.stream(values()).filter(taskType ->
                taskType.getEventType().equals(event)).collect(Collectors.toList());
    }
}
