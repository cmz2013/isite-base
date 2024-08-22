package org.isite.operation.data.vo;

import java.util.List;

/**
 * @Description 奖品任务自定义属性
 * @Author <font color='blue'>zhangcm</font>
 */
public class PrizeTaskProperty extends TaskProperty<PrizeReward> {

    /**
     * 重写Getter方法是为了解决toJsonFields(PrizeTaskProperty.class)时，父类方法获取不到泛型参数PrizeReward
     */
    @Override
    public List<PrizeReward> getRewards() {
        return super.getRewards();
    }
}
