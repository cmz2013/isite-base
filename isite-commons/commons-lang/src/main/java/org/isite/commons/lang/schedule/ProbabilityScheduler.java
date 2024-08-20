package org.isite.commons.lang.schedule;

import java.util.List;
import java.util.function.ToIntFunction;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.schedule.RandomScheduler.nextInt;

/**
 * @description 使用概率进行随机调度
 * @author <font color='blue'>zhangcm</font>
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
            total += list.get(i).getCoefficient();
            weightArray[i] = total;
        }
        return choose(list, total, weightArray);
    }

    private static <T> T choose(List<T> list, int total, int[] weightArray) {
        if (total > ZERO) {
            int r = nextInt(total);
            for (int i = ZERO; i < weightArray.length; i++) {
                if (r < weightArray[i]) {
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
}
