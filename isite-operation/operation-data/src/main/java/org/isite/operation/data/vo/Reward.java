package org.isite.operation.data.vo;

import org.isite.commons.lang.json.Comment;
import org.isite.commons.lang.schedule.Model;

import java.io.Serializable;

/**
 * 运营任务奖励：完成运营任务时发放奖励（保存在任务记录中）。
 *
 * 用于选取奖励的算法系数coefficient注意事项:
 * 1）如果任务只配置一个奖励，不需要配置coefficient，或者也可以用于配置奖励发放条件
 * 2）如果任务配置了多个奖励，coefficient默认用于配置: 概率/10000，随机选取奖励
 * @author <font color='blue'>zhangcm</font>
 */
public class Reward extends Model implements Serializable {

    @Override
    @Comment("概率")
    public Integer getCoefficient() {
        return super.getCoefficient();
    }
}
