package org.isite.commons.lang;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.schedule.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.isite.commons.lang.Reflection.toJsonFields;
import static org.isite.commons.lang.json.Jackson.toJsonString;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
public class ReflectionTest {

    private Reward[] rewards;

    public static void main(String[] args) {
        System.out.println(toJsonString(toJsonFields(ReflectionTest.class)));
        System.out.println(toJsonString(toJsonFields(WelfareTaskProperty.class)));

        WelfareTaskProperty property = new WelfareTaskProperty();
        property.setCumulateAmount(1000);
        property.setStartTime(new Date(System.currentTimeMillis()));
        List<PrizeReward> rewards = new ArrayList<>();
        PrizeReward reward = new PrizeReward();
        reward.setPrizeId(1);
        rewards.add(reward);
        reward = new PrizeReward();
        reward.setPrizeId(2);
        rewards.add(reward);
        property.setRewards(rewards);
        System.out.println(toJsonString(toJsonFields(property)));
    }

    @Getter
    @Setter
    public static class WelfareTaskProperty extends PrizeTaskProperty {
        private Date startTime;
        private Integer cumulateAmount;
    }

    public static class PrizeTaskProperty extends TaskProperty<PrizeReward> {
        @Override
        public List<PrizeReward> getRewards() {
            return super.getRewards();
        }
    }

    @Getter
    @Setter
    public static class PrizeReward extends Reward {
        private Integer prizeId;
    }

    public static class Reward extends Model implements Serializable {
    }

    @Getter
    @Setter
    public static class TaskProperty<T extends Reward> implements Serializable {
        /**
         * 运营任务奖励列表
         */
        private List<T> rewards;
    }
}
