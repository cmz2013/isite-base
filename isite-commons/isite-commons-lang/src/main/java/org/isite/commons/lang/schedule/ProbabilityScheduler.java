package org.isite.commons.lang.schedule;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Constants;

import java.util.List;
import java.util.function.ToIntFunction;
/**
 * @Description 使用概率进行随机调度
 * @Author <font color='blue'>zhangcm</font>
 */
public class ProbabilityScheduler {
    private ProbabilityScheduler() {
    }

    public static <T extends Model> T choose(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        int total = Constants.ZERO;
        int[] weightArray = new int[list.size()];
        for (int i = Constants.ZERO; i < list.size(); i++) {
            total += list.get(i).getWeight();
            weightArray[i] = total;
        }
        return choose(list, total, weightArray);
    }

    private static <T> T choose(List<T> list, int total, int[] weightArray) {
        if (total > Constants.ZERO) {
            int weight = RandomScheduler.nextInt(total);
            for (int i = Constants.ZERO; i < weightArray.length; i++) {
                if (weight <= weightArray[i]) {
                    return list.get(i);
                }
            }
        }
        return null;
    }

    public static <T> T choose(List<T> list, ToIntFunction<T> probability) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        int total = Constants.ZERO;
        int[] weightArray = new int[list.size()];
        for (int i = Constants.ZERO; i < list.size(); i++) {
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
