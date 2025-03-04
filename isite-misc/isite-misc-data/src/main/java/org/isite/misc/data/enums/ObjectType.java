package org.isite.misc.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.isite.commons.lang.enums.Enumerable;
/**
 * @Description 对象类型，和数据模型一一对应
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ObjectType implements Enumerable<Integer> {
    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 企业员工
     */
    TENANT_EMPLOYEE(21, "员工"),

    SHOP_TRADE_ORDER(31, "订单"),

    QUESTION(81, "疑问"),
    QUESTION_ANSWER(82, "答案"),

    /**
     * 运营活动
     */
    OPERATION_ACTIVITY(91, "运营活动"),
    /**
     * 签到日志
     */
    OPERATION_SIGN_LOG(92, "签到日志"),
    OPERATION_INVITE_RECORD(93, "邀请记录")
    ;

    private final Integer code;
    @lombok.Getter
    private final String label;

    ObjectType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }
}
