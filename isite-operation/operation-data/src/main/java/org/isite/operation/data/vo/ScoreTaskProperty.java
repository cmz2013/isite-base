package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description 积分任务自定义属性
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ScoreTaskProperty extends TaskProperty<ScoreReward> {

    /**
     * 重写Getter方法是为了解决toJsonFields(ScoreTaskProperty.class)时，父类方法获取不到泛型参数ScoreReward
     */
    @Override
    public List<ScoreReward> getRewards() {
        return super.getRewards();
    }
}
