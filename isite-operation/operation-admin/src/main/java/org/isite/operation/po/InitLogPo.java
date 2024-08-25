package org.isite.operation.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "init_log")
public class InitLogPo extends Po<Long> {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 活动ID
     */
    private Integer activityId;
}
