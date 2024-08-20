package org.isite.operation.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotBlank;

import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizePutDto extends Dto<Integer> {
    /**
     * 奖品名称
     */
    @NotBlank(groups = Update.class)
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
    private Integer totalInventory = ZERO;
    /**
     * 奖品排列顺序
     */
    private Integer sequence;
    /**
     * 备注
     */
    private String remark;
}
