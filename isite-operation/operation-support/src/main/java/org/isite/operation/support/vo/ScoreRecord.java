package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;
import org.isite.operation.support.enums.ScoreType;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ScoreRecord extends Vo<Long> {
    /**
     * 积分类型
     */
    private ScoreType scoreType;
    /**
     * 积分值
     */
    private Integer scoreValue;
}
