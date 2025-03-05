package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.operation.support.enums.PrizeType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizePostDto {
    /**
     * 活动id，不能编辑
     */
    @NotNull
    private Integer activityId;
    /**
     * 奖品名称
     */
    @NotBlank
    private String prizeName;
    /**
     * 奖品类型
     */
    @NotNull
    private PrizeType prizeType;
    /**
     * 第三方奖品值
     */
    private String thirdPrizeValue;
    /**
     * 奖品图片
     */
    private String prizeImage;
    /**
     * 奖品排列顺序
     */
    private Integer sort;
    /**
     * 总库存，0不做库存校验
     */
    @NotNull
    private Integer totalInventory;
    /**
     * 中奖概率/100000
     */
    private Integer probability;
    /**
     * 备注
     */
    private String remark;
}
