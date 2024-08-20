package org.isite.commons.lang.schedule;

import lombok.Getter;
import lombok.Setter;

/**
 * 调度数据模型父类
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Model {
    /**
     * 算法系数（eg：概率）
     */
    private Integer coefficient;
}
