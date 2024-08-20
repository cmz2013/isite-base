package org.isite.commons.lang.json;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class JsonField {
    /**
     * @description 字段类型
     */
    private Type type;
    /**
     * @description 字段名
     */
    private String name;
    /**
     * @description 数据
     * 转换当前对象类或父类或父接口的所有字段（必须有Getter方法）为JsonField时，
     * 当前字段Object类型，data：List<JsonField>
     * 当前字段Array类型，data：List<List<JsonField>>
     * 当前字段为Number、String、Boolean等类型时，data为值
     */
    private Object data;
    /**
     * 字段说明
     */
    private String comment;
}
