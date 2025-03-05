package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Description 奖品交付
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeDeliver extends Vo<Integer> {
    /**
     * 领奖记录ID
     */
    private Long prizeRecordId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 地区编码
     */
    private String regionCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 订单号
     */
    private String orderNum;
    /**
     * 交付时间
     */
    private LocalDateTime deliverTime;
}
