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
    SYSTEM_ADMIN(2, "系统管理后台"),
    SECURITY_CENTER(3, "认证鉴权中心"),
    TENANT_SERVICE(4, "租户服务"),
    DATA_ADMIN(5, "数据集成管理后台"),
    JOB_ADMIN(6, "定时调度管理后台"),
    EXAM_SERVICE(7, "考试服务"),
    OPERATION_SERVICE(8, "运营服务"),
    INQUIRY_SERVICE(9, "答疑解惑服务"),
    SHOP_SERVICE(10, "商城服务");

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
