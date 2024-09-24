package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;

import java.util.Date;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class SignLog extends Vo<Long> {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
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
