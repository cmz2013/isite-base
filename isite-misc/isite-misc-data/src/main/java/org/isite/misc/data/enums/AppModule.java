package org.isite.misc.data.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.isite.commons.lang.enums.Enumerable;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.OBJECT;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@JsonFormat(shape = OBJECT)
public enum AppModule implements Enumerable<Integer> {
    USER_CENTER(1, "用户中心"),
    TENANT_ADMIN(2, "租户服务"),
    EXAM_ADMIN(3, "考试服务"),
    OPERATION_ADMIN(4, "运营服务"),
    QUESTION_ADMIN(5, "答疑解惑"),
    SHOP_ADMIN(6, "商城服务");

    private final Integer code;
    private final String label;

    AppModule(Integer code, String label) {
        this.code = code;
        this.label = label;
    }

    @Override
    public  Integer getCode() {
        return this.code;
    }

    public String getLabel() {
        return label;
    }
}
