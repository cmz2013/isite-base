package org.isite.operation.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.operation.data.vo.AnswerPrizeProperty;
import org.isite.operation.data.vo.OrderPrizeProperty;
import org.isite.operation.data.vo.PrizeTaskProperty;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.ScoreTaskProperty;
import org.isite.operation.data.vo.SignScoreProperty;
import org.isite.operation.data.vo.TaskProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.isite.operation.data.enums.EventType.GET_OPERATION_WEBPAGE;
import static org.isite.operation.data.enums.EventType.POST_OPERATION_SIGN;
import static org.isite.operation.data.enums.EventType.POST_QUESTION;
import static org.isite.operation.data.enums.EventType.POST_QUESTION_ANSWER;
import static org.isite.operation.data.enums.EventType.POST_SHOP_ORDER_PAYMENT_NOTIFY;
import static org.isite.operation.data.enums.EventType.PUT_QUESTION_ANSWER_ADOPT;
import static org.isite.operation.data.enums.EventType.PUT_USER;

/**
 * @Description 运营任务类型。
 * 配置运营活动时，先选择行为类型（EventType），再根据行为类型查询和选择任务类型（TaskType）
 * 使用 @JsonFormat 注解可以将枚举类转为json再返回给前端
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum TaskType implements Enumerable<Integer> {

    /**
     * 积分类型任务
     */
    USER_SCORE(1, PUT_USER, "提交个人信息送积分", ScoreTaskProperty.class),
    OPERATION_SIGN_SCORE(2, POST_OPERATION_SIGN, "每日签到送积分", SignScoreProperty.class),

    /**
     * 奖品类型任务
     */
    QUESTION_PRIZE(101, POST_QUESTION, "提交问题送奖品", PrizeTaskProperty.class),
    QUESTION_ANSWER_PRIZE(102, POST_QUESTION_ANSWER, "答疑送奖品", AnswerPrizeProperty.class),
    //提问人采纳答案时，给回复人送奖品。eventDto#userId为提问人ID，eventParam为回复人ID TODO
    QUESTION_ANSWER_ADOPT_PRIZE(103, PUT_QUESTION_ANSWER_ADOPT, "答案被采纳送奖品", AnswerPrizeProperty.class),
    SHOP_ORDER_PRIZE(104, POST_SHOP_ORDER_PAYMENT_NOTIFY, "老用户福利", OrderPrizeProperty.class),

    /**
     * 邀请类型任务（邀请码不为空），保存邀请记录再送奖励
     */
    QUESTION_ANSWER_INVITE(201, POST_QUESTION_ANSWER, "答疑邀请", PrizeTaskProperty.class),
    OPERATION_WEBPAGE_INVITE(202, GET_OPERATION_WEBPAGE, "活动邀请", PrizeTaskProperty.class),
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
        return stream(values()).filter(taskType -> taskType.getEventType().equals(event)).collect(toList());
    }
}
