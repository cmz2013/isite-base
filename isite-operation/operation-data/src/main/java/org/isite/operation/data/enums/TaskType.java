package org.isite.operation.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.operation.data.vo.OrderPrizeProperty;
import org.isite.operation.data.vo.PrizeTaskProperty;
import org.isite.operation.data.vo.ReplyPrizeProperty;
import org.isite.operation.data.vo.Reward;
import org.isite.operation.data.vo.ScoreTaskProperty;
import org.isite.operation.data.vo.SignScoreProperty;
import org.isite.operation.data.vo.TaskProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.isite.operation.data.enums.EventType.GET_ACTIVITY_WEBPAGE;
import static org.isite.operation.data.enums.EventType.POST_INQUIRY;
import static org.isite.operation.data.enums.EventType.POST_ORDER_PAYMENT_NOTIFY;
import static org.isite.operation.data.enums.EventType.POST_REPLY;
import static org.isite.operation.data.enums.EventType.POST_SIGN;
import static org.isite.operation.data.enums.EventType.PUT_REPLY_ACCEPT;
import static org.isite.operation.data.enums.EventType.PUT_USER;

/**
 * @description 运营任务类型。
 * 配置运营活动时，先选择行为类型（EventType），再根据行为类型查询和选择任务类型（TaskType）
 * 使用 @JsonFormat 注解可以将枚举类转为json再返回给前端
 * @author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum TaskType implements Enumerable<Integer> {

    /**
     * 积分类型任务
     */
    SIGN_SCORE(1, POST_SIGN, "每日签到送积分", SignScoreProperty.class),
    USER_SCORE(2, PUT_USER, "提交个人信息送积分", ScoreTaskProperty.class),
    /**
     * 邀请好友送积分（邀请码不为空），保存邀请记录再送积分 TODO
     */
    ACTIVITY_INVITE_SCORE(3, GET_ACTIVITY_WEBPAGE, "邀请好友送积分", ScoreTaskProperty.class),
    REPLY_INVITE_SCORE(4, POST_REPLY, "邀请答疑送积分", ScoreTaskProperty.class),
    //给提问人送积分
    ACCEPT_REPLY_SCORE(5, PUT_REPLY_ACCEPT, "采纳答案送积分", ScoreTaskProperty.class),

    /**
     * 奖品类型任务
     * TODO 保存邀请记录再送奖品（邀请码不为空）
     */
    REPLY_INVITE_PRIZE(101, POST_REPLY, "邀请答疑送奖品", PrizeTaskProperty.class),
    INQUIRY_PRIZE(102, POST_INQUIRY, "提交疑问送奖品", PrizeTaskProperty.class),
    REPLY_PRIZE(103, POST_REPLY, "提交回答送奖品", ReplyPrizeProperty.class),
    //给回答人送奖品
    ADOPT_REPLY_PRIZE(104, PUT_REPLY_ACCEPT, "回答被采纳送奖品", ReplyPrizeProperty.class),
    ORDER_PRIZE(105, POST_ORDER_PAYMENT_NOTIFY, "老用户奖品福利", OrderPrizeProperty.class)
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
     * @description 当前行为类型可以触发的任务类型
     */
    public static List<TaskType> values(EventType event) {
        return stream(values()).filter(taskType -> taskType.getEventType().equals(event)).collect(toList());
    }
}
