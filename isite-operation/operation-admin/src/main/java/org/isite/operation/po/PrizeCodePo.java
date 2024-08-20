package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * 兑奖码。在兑奖码管理列表，先选择一个兑奖码类型的活动奖品，然后批量导入兑奖码
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "prize_code")
public class PrizeCodePo extends Po<Integer> {
    /**
     * 奖品id
     */
    private Integer prizeId;
    /**
     * 编码
     */
    private String code;
    /**
     * 活动ID
     */
    private Integer activityId;
    /**
     * 领取人id
     */
    private Long userId;

    public PrizeCodePo() {
        super();
    }

    public PrizeCodePo(Integer activityId, Integer prizeId, String code) {
        super();
        this.activityId = activityId;
        this.prizeId = prizeId;
        this.code = code;
    }
}
