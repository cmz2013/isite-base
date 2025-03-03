package org.isite.operation.support.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.commons.lang.json.Jackson;
import org.isite.misc.data.enums.AppModule;
import org.isite.misc.data.enums.ObjectType;
import org.isite.operation.support.vo.AnswerEventParam;
import org.isite.operation.support.vo.InviteEventParam;

import java.util.function.Function;
/**
 * @Description 业务行为类型枚举类，满足以下两个条件：
 * 1）行为类型和前端页面调用接口一一对应
 * 2）行为类型也可以是系统任务，比如定时任务
 * 配置运营活动时，先选择行为类型（EventType），再根据行为类型查询和选择任务类型（TaskType）
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventType implements Enumerable<Integer> {
    /**
     * 完善个人信息接口 PUT /user
     */
    PUT_USER(1, "完善个人信息", ObjectType.USER, AppModule.USER_CENTER, null),

    /**
     * 用户提交疑问接口 POST /inquiry
     */
    POST_QUESTION(101, "提交疑问", ObjectType.QUESTION, AppModule.QUESTION_ADMIN, null),
    /**
     * 用户答疑接口，被邀请人url携带邀请码：POST /question/{questionId}/reply?invite={code}
     */
    POST_QUESTION_REPLY(102, "答疑解惑", ObjectType.QUESTION_ANSWER, AppModule.QUESTION_ADMIN,
            eventParam -> Jackson.parseObject(eventParam.toString(), AnswerEventParam.class)),
    /**
     * 提问人采纳答案接口 PUT /reply/{replyId}/adopt
     * 发送行为消息时，EventDto#userId用于传递提问人ID，EventDto#eventParam用于传递 回答人ID
     */
    PUT_QUESTION_REPLY_ADOPT(103, "采纳答案", ObjectType.QUESTION_ANSWER, AppModule.QUESTION_ADMIN, null),

    /**
     * 订单完成支付回调接口
     * POST /api/shop/wxpay/notify
     * POST /api/shop/alipay/notify
     */
    POST_SHOP_PAYMENT_NOTIFY(301, "订单完成支付", ObjectType.SHOP_TRADE_ORDER, AppModule.SHOP_ADMIN, null),
    /**
     * 每日签到接口 POST /sign
     * 发送行为消息EventDto时，行为参数用于传递 连续签到天数
     */
    POST_OPERATION_SIGN(401, "每日签到", ObjectType.OPERATION_SIGN_LOG, AppModule.OPERATION_ADMIN, null),
    /**
     * 活动页面接口 GET /webpage/{activityId}?invite={邀请码}
     */
    GET_OPERATION_WEBPAGE(402, "访问活动页面", ObjectType.OPERATION_ACTIVITY, AppModule.OPERATION_ADMIN,
            eventParam -> Jackson.parseObject(eventParam.toString(), InviteEventParam.class)),
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
    private final Function<Object, ?> converter;

    EventType(Integer code, String label, ObjectType objectType, AppModule appModule, Function<Object, ?> converter) {
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
