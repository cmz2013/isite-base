package org.isite.operation.data.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description 邀请任务自定义属性
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class InviteTaskProperty extends TaskProperty<InviteReward> {

    /**
     * 重写Getter方法是为了解决toJsonFields(InviteTaskProperty.class)时，父类方法获取不到泛型参数InviteReward
     */
    @Override
    public List<InviteReward> getRewards() {
        return super.getRewards();
    }
}
