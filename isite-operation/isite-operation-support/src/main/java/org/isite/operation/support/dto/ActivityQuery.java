package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.ActiveStatus;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ActivityQuery {
    /**
     * 主活动ID
     */
    private Integer pid;
    /**
     * 上架状态
     */
    private ActiveStatus status;
}
