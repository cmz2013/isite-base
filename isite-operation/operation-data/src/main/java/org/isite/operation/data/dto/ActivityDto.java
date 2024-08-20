package org.isite.operation.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.operation.data.enums.ActivityTheme;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ActivityDto extends Dto<Integer> {
    /**
     * 主活动ID
     */
    private Integer pid;
    /**
     * 活动标题
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String title;
    /**
     * 活动主题
     */
    @NotNull(groups = {Add.class, Update.class})
    private ActivityTheme theme;
    /**
     * 活动属性
     */
    private String property;
    /**
     * 开始时间
     */
    @NotNull(groups = {Add.class, Update.class})
    private Date startTime;
    /**
     * 结束时间
     */
    @NotNull(groups = {Add.class, Update.class})
    private Date endTime;
    /**
     * 备注
     */
    private String remark;
}
