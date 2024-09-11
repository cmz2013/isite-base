package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.operation.support.enums.ScoreType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Description 积分任务记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "score_record")
public class ScoreRecordPo extends TaskRecordPo {
    /**
     * 积分值
     */
    private Integer scoreValue;
    /**
     * 积分类型
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ScoreType scoreType;
    /**
     * 已使用积分
     */
    private Integer usedScore;
}
