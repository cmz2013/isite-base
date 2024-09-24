package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;
import org.isite.operation.support.enums.PrizeType;

/**
 * @Description 运营活动奖品
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Prize extends Vo<Integer> {
    /**
     * 活动id。奖品根据活动进行数据隔离，避免并发冲突，支撑某个活动临时下线修改奖品
     */
    private Integer activityId;
    /***
     * 奖品名称
     */
    private String prizeName;
    /**
     * 奖品类型
     */
    private PrizeType prizeType;
    /**
     * 第三方奖品 值
     */
    private String thirdPrizeValue;
    /**
     * 奖品图片
     */
    private String prizeImage;
    /**
     * 总库存，0不做库存校验
     */
    private Integer totalInventory;
    /**
     * 中奖概率/100000
     */
    private Integer probability;
    /**
     * 奖品排列顺序
     */
    private Integer sort;
    /**
     * 已消耗库存
     */
    private Integer consumeInventory;
    /**
     * 已锁定库存
     */
    private Integer lockInventory;
    /**
     * 备注
     */
    private String remark;
}
