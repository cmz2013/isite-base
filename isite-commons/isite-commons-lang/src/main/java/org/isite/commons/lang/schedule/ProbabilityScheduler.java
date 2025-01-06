package org.isite.commons.lang.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.ToIntFunction;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.schedule.RandomScheduler.nextInt;

/**
 * @Description 使用概率进行随机调度
 * @Author <font color='blue'>zhangcm</font>
 */
public class ProbabilityScheduler {
    private ProbabilityScheduler() {
    }

    public static <T extends Model> T choose(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        int total = ZERO;
        int[] weightArray = new int[list.size()];
        for (int i = ZERO; i < list.size(); i++) {
            total += list.get(i).getWeight();
            weightArray[i] = total;
        }
        return choose(list, total, weightArray);
    }

    private static <T> T choose(List<T> list, int total, int[] weightArray) {
        if (total > ZERO) {
            int weight = nextInt(total);
            for (int i = ZERO; i < weightArray.length; i++) {
                if (weight <= weightArray[i]) {
                    return list.get(i);
                }
            }
        }
        return null;
    }

    public static <T> T choose(List<T> list, ToIntFunction<T> probability) {
        if (isEmpty(list)) {
            return null;
        }
        int total = ZERO;
        int[] weightArray = new int[list.size()];
        for (int i = ZERO; i < list.size(); i++) {
            total += probability.applyAsInt(list.get(i));
            weightArray[i] = total;
        }
        return choose(list, total, weightArray);
    }

    /**
     * @Description 调度数据模型父类
     * @Author <font color='blue'>zhangcm</font>
     */
    @Getter
    @Setter
    public static class Model {
        /**
         * 权重
         */
        private Integer weight;
    }
}
