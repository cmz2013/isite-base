package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;

/**
 * @Description 运营任务奖励：奖品
 * 1）通过发送行为消息，运营任务异步统计数据赠送领奖记录（待抽奖），可以解决实时查询时统计数据的性能问题
 * 2）活动奖品必须通过活动接口同步发放，防止异步操作时出现并发锁冲突、库存不足等原因无法发放，信息不能同步给用户
 * 3）任务奖励比如积分、抽奖机会、领奖记录（待抽奖）等，是通过行为消息异步保存，不存在并发锁冲突、库存不足等原因导致无法发放
 * 4）任务奖励通过行为消息如果发放异常失败，则会直接丢弃
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class PrizeReward extends Reward {
    /**
     * 1）任务奖励在奖品记录中保存奖品ID，但是不锁定奖品，只能通过管理页面设置抽奖必中
     * 2）执行一次任务，只能奖励一个奖品，即新增一条奖品记录，不支持多个奖品。但是，一个活动可以创建多个同一类型的任务
     */
    @Comment("奖品ID")
    private Integer prizeId;
}

