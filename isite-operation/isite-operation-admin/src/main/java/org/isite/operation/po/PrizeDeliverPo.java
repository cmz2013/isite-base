package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 奖品交付信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "prize_deliver")
public class PrizeDeliverPo extends Po<Integer> {
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
    private Date deliverTime;
    /**
     * 交付负责人员工id （FR：Fulfill Responsibility）
     */
    private Long frId;
}
