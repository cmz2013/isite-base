package org.isite.operation.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.isite.commons.lang.enums.Enumerable;
import org.isite.misc.data.enums.AppModule;
import org.isite.misc.data.enums.ObjectType;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;
import static org.isite.misc.data.enums.AppModule.INQUIRY_SERVICE;
import static org.isite.misc.data.enums.AppModule.OPERATION_SERVICE;
import static org.isite.misc.data.enums.AppModule.SHOP_SERVICE;
import static org.isite.misc.data.enums.AppModule.USER_CENTER;
import static org.isite.misc.data.enums.ObjectType.ACTIVITY;
import static org.isite.misc.data.enums.ObjectType.INQUIRY;
import static org.isite.misc.data.enums.ObjectType.ORDER;
import static org.isite.misc.data.enums.ObjectType.REPLY;
import static org.isite.misc.data.enums.ObjectType.SIGN_LOG;
import static org.isite.misc.data.enums.ObjectType.USER;

/**
 * 业务行为类型枚举类，满足以下两个条件：
 * 1）行为类型和前端页面调用接口一一对应
 * 2）行为类型也可以是系统任务，比如定时任务
 *
 * 配置运营活动时，先选择行为类型（EventType），再根据行为类型查询和选择任务类型（TaskType）
 * @author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum EventType implements Enumerable<Integer> {
    /**
     * 完善个人信息接口 PUT /user
     */
    PUT_USER(1, "完善个人信息", USER, USER_CENTER),

    /**
     * 用户提交疑问接口 POST /inquiry
     */
    POST_INQUIRY(101, "提交疑问", INQUIRY, INQUIRY_SERVICE),
    /**
     * 用户答疑接口，被邀请人url携带邀请码：POST /inquiry/{inquiryId}/reply?invite={code}
     * 发送行为消息EventDto时，行为参数用于传递 ReplyEventParam
     */
    POST_REPLY(102, "答疑解惑", REPLY, INQUIRY_SERVICE),
    /**
     * 提问人采纳答案接口 PUT /reply/{replyId}/accept
     * 发送行为消息时，EventDto#userId用于传递提问人ID，EventDto#eventParam用于传递 回答人ID
     */
    PUT_REPLY_ACCEPT(103, "采纳答案", REPLY, INQUIRY_SERVICE),

    /**
     * 订单完成支付回调接口 POST /order/payment/notify
     */
    POST_ORDER_PAYMENT_NOTIFY(301, "订单完成支付", ORDER, SHOP_SERVICE),
    /**
     * 每日签到接口 POST /sign
     * 发送行为消息EventDto时，行为参数用于传递 连续签到天数
     */
    POST_SIGN(401, "每日签到", SIGN_LOG, OPERATION_SERVICE),
    /**
     * 活动页面接口 GET /webpage/{activityId}?invite={邀请码}
     */
    GET_ACTIVITY_WEBPAGE(402, "访问活动页面", ACTIVITY, OPERATION_SERVICE),
    ;

    private final Integer code;
    private final String label;
    /**
     * 业务对象类型
     */
    private final ObjectType objectType;
    /**
     * 应用模块
     */
    private final AppModule appModule;

    EventType(Integer code, String label, ObjectType objectType, AppModule appModule) {
        this.code = code;
        this.label = label;
        this.objectType = objectType;
        this.appModule = appModule;
    }

    @Override
    public  Integer getCode() {
        return this.code;
    }

    public String getLabel() {
        return label;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public AppModule getAppModule() {
        return appModule;
    }
}
