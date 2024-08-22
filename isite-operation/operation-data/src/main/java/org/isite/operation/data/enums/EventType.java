package org.isite.operation.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.isite.commons.lang.Functions;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.misc.data.enums.AppModule;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.data.vo.AnswerEventParam;
import org.isite.operation.data.vo.InviteEventParam;

import java.util.function.Function;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.misc.data.enums.AppModule.OPERATION_ADMIN;
import static org.isite.misc.data.enums.AppModule.QUESTION_ADMIN;
import static org.isite.misc.data.enums.AppModule.SHOP_ADMIN;
import static org.isite.misc.data.enums.AppModule.USER_CENTER;
import static org.isite.misc.data.enums.ObjectType.OPERATION_ACTIVITY;
import static org.isite.misc.data.enums.ObjectType.OPERATION_SIGN_LOG;
import static org.isite.misc.data.enums.ObjectType.QUESTION;
import static org.isite.misc.data.enums.ObjectType.QUESTION_ANSWER;
import static org.isite.misc.data.enums.ObjectType.SHOP_ORDER;
import static org.isite.misc.data.enums.ObjectType.USER;

/**
 * @Description 业务行为类型枚举类，满足以下两个条件：
 * 1）行为类型和前端页面调用接口一一对应
 * 2）行为类型也可以是系统任务，比如定时任务
 * 配置运营活动时，先选择行为类型（EventType），再根据行为类型查询和选择任务类型（TaskType）
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum EventType implements Enumerable<Integer> {
    /**
     * 完善个人信息接口 PUT /user
     */
    PUT_USER(1, "完善个人信息", USER, USER_CENTER, null),

    /**
     * 用户提交疑问接口 POST /inquiry
     */
    POST_QUESTION(101, "提交疑问", QUESTION, QUESTION_ADMIN, null),
    /**
     * 用户答疑接口，被邀请人url携带邀请码：POST /inquiry/{inquiryId}/reply?invite={code}
     * 发送行为消息EventDto时，行为参数用于传递 ReplyEventParam
     */
    POST_QUESTION_ANSWER(102, "答疑解惑", QUESTION_ANSWER, QUESTION_ADMIN,
            eventParam -> parseObject(eventParam.toString(), AnswerEventParam.class)),
    /**
     * 提问人采纳答案接口 PUT /reply/{replyId}/accept
     * 发送行为消息时，EventDto#userId用于传递提问人ID，EventDto#eventParam用于传递 回答人ID
     */
    PUT_QUESTION_ANSWER_ADOPT(103, "采纳答案", QUESTION_ANSWER, QUESTION_ADMIN, null),

    /**
     * 订单完成支付回调接口 POST /order/payment/notify
     */
    POST_SHOP_ORDER_PAYMENT_NOTIFY(301, "订单完成支付", SHOP_ORDER, SHOP_ADMIN, null),
    /**
     * 每日签到接口 POST /sign
     * 发送行为消息EventDto时，行为参数用于传递 连续签到天数
     */
    POST_OPERATION_SIGN(401, "每日签到", OPERATION_SIGN_LOG, OPERATION_ADMIN, null),
    /**
     * 活动页面接口 GET /webpage/{activityId}?invite={邀请码}
     */
    GET_OPERATION_WEBPAGE(402, "访问活动页面", OPERATION_ACTIVITY, OPERATION_ADMIN,
            eventParam -> parseObject(eventParam.toString(), InviteEventParam.class)),
    ;

    private final Integer code;
    @Getter
    private final String label;
    /**
     * 业务对象类型
     */
    @Getter
    private final ObjectType objectType;
    /**
     * 应用模块
     */
    @Getter
    private final AppModule appModule;

    /**
     * 在EventConsumer执行任务之前，需要根据行为类型进行解析和转换行为参数。
     */
    @Getter
    private Function<Object, Object> converter;

    EventType(Integer code, String label, ObjectType objectType, AppModule appModule, Functions<Object, Object> converter) {
        this.code = code;
        this.label = label;
        this.objectType = objectType;
        this.appModule = appModule;
        this.converter = converter;
    }

    @Override
    public  Integer getCode() {
        return this.code;
    }

}
