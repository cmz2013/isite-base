package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "init_log")
public class InitLogPo extends Po<Long> {
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 用户id
     */
    private Long userId;
}
