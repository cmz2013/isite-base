package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;
import org.isite.commons.lang.Constants;

import javax.validation.constraints.NotBlank;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizePutDto extends Dto<Integer> {
    /**
     * 奖品名称
     */
    @NotBlank
    private String prizeName;
    /**
     * 第三方奖品值
     */
    private String thirdPrizeValue;
    /**
     * 奖品图片
     */
    private String prizeImage;
    /**
     * 中奖概率
     */
    private Integer probability;
    /**
     * 总库存，0不做库存校验。
     * 兑奖码的总库存不能编辑，导入或删除兑奖码时维护
     */
    private Integer totalInventory = Constants.ZERO;
    /**
     * 奖品排列顺序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
}
