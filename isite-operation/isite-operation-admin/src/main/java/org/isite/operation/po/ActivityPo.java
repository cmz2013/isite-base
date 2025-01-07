package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.operation.support.enums.ActivityTheme;
import org.isite.operation.support.vo.ActivityProperty;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.util.Date;

/**
 * @description 运营活动
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "activity")
public class ActivityPo extends Po<Integer> {
    /**
     * 主活动ID（主子活动，支持两级）
     */
    private Integer pid;
    /**
     * 活动名称
     */
    private String title;
    /**
     * 活动主题
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ActivityTheme theme;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * @see ActivityProperty
     * 活动属性JSON
     */
    private String property;
    /**
     * 上架状态
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private ActiveStatus status;
    /**
     * 备注
     */
    private String remark;
}
