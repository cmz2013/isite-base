package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.operation.data.enums.PrizeType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * 运营活动奖品
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "prize")
public class PrizePo extends Po<Integer> {
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 奖品类型
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
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
     * 总库存，等于0时，不做库存校验
     */
    private Integer totalInventory;
    /**
     * 中奖概率/100000
     */
    private Integer probability;
    /**
     * 已消耗库存
     */
    private Integer consumeInventory;
    /**
     * 已锁定库存
     */
    private Integer lockInventory;
    /**
     * 奖品排列顺序
     */
    private Integer sequence;
    /**
     * 备注
     */
    private String remark;
}
