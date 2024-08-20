package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 积分任务自定义属性
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class SignScoreProperty extends TaskProperty<SignScoreReward> {

    /**
     * 重写Getter方法是为了解决toJsonFields(SignTaskProperty.class)时，父类方法获取不到泛型参数ScoreReward
     */
    @Override
    public List<SignScoreReward> getRewards() {
        return super.getRewards();
    }
}
