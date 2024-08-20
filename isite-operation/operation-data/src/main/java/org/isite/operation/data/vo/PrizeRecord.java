package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.operation.data.enums.PrizeType;

import java.util.Date;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeRecord extends Vo<Long> {
    /**
     * 运营奖品id
     */
    private Integer prizeId;
    /**
     * 领奖状态：true 已领取
     */
    private Boolean receiveStatus;
    /**
     * 领取时间
     */
    private Date receiveTime;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 奖品类型
     */
    private PrizeType prizeType;
    /**
     * 第三方奖品值
     */
    private String thirdPrizeValue;
    /**
     * 奖品图片
     */
    private String prizeImage;
}
