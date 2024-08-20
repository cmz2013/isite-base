package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;
import java.util.Date;

/**
 * 每日签到
 *
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "sign_log")
public class SignLogPo extends Po<Long> {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 连续签到天数
     */
    private Integer continuousCount;
    /**
     * 累计签到天数
     */
    private Integer totalCount;
    /**
     * 签到时间
     */
    private Date signTime;
}
