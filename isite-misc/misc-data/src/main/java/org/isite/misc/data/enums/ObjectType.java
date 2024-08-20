package org.isite.misc.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.isite.commons.lang.enums.Enumerable;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;

/**
 * @Description 对象类型，和数据模型一一对应
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum ObjectType implements Enumerable<Integer> {
    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 企业员工
     */
    EMPLOYEE(21, "员工"),

    ORDER(31, "订单"),

    INQUIRY(81, "疑问"),
    REPLY(82, "回答"),

    /**
     * 运营活动
     */
    ACTIVITY(91, "活动"),
    /**
     * 签到日志
     */
    SIGN_LOG(92, "签到")
    ;

    private final Integer code;
    private final String label;

    ObjectType(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    public String getLabel() {
        return label;
    }
}
