package org.isite.commons.lang;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.json.Comment;
import org.isite.commons.lang.schedule.ProbabilityScheduler;

import java.util.ArrayList;
import java.util.List;

import static org.isite.commons.lang.Reflection.toJsonFields;
import static org.isite.commons.lang.json.Jackson.toJsonString;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class JsonFieldTest {

    @Comment("运营任务奖励列表")
    private Reward[] rewards;

    public static void main(String[] args) {
        System.out.println(toJsonString(toJsonFields(JsonFieldTest.class)));
        System.out.println(toJsonString(toJsonFields(PrizeTaskProperty.class)));

        PrizeTaskProperty property = new PrizeTaskProperty();
        List<PrizeReward> rewards = new ArrayList<>();
        PrizeReward reward = new PrizeReward();
        reward.setPrizeId(1);
        reward.setWeight(90);
        rewards.add(reward);
        reward = new PrizeReward();
        reward.setPrizeId(2);
        reward.setWeight(10);
        rewards.add(reward);
        property.setRewards(rewards);
        System.out.println(toJsonString(toJsonFields(property)));
        System.out.println(new GetterFieldNameTest().test(GetterFieldNameTest::getData));
    }

    public static class PrizeTaskProperty extends TaskPropertyTest<PrizeReward> {
        @Comment("运营任务奖品列表")
        @Override
        public List<PrizeReward> getRewards() {
            return super.getRewards();
        }
    }

    @Getter
    @Setter
    public static class TaskPropertyTest<T extends Reward> {
        private List<T> rewards;
    }

    public static class Reward extends ProbabilityScheduler.Model {

        @Override
        @Comment("奖品权重")
        public Integer getWeight() {
            return super.getWeight();
        }
    }

    @Getter
    @Setter
    public static class PrizeReward extends Reward {

        @Comment("奖品ID")
        private Integer prizeId;
    }
}
