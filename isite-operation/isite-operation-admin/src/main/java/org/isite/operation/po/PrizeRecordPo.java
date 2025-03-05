package org.isite.operation.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.type.EnumTypeHandler;
import org.isite.operation.support.enums.PrizeType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.time.LocalDateTime;
/**
 * @Description 奖品记录。一条记录只保存的奖品数量为1，不支持多个
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "prize_record")
public class PrizeRecordPo extends TaskRecordPo {
    /**
     * 运营奖品id
     */
    private Integer prizeId;
    /**
     * 锁定状态：用于设置抽奖必中
     * 设置为true时，必中奖品 已消耗库存-1，锁定库存+1；用户完成抽奖时锁定库存不能小于1，锁定库存-1， 不更新已消耗库存。
     * lock_status为false时，即普通抽奖，用户完成抽奖时更新已消耗库存，不处理已锁定库存
     */
    private Boolean lockStatus;
    /**
     * 领奖状态：true 已领取
     */
    private Boolean receiveStatus;
    /**
     * 领取时间
     */
    private LocalDateTime receiveTime;
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
}
