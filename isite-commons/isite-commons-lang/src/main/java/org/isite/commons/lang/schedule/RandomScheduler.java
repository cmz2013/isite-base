package org.isite.commons.lang.schedule;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Random;
/**
 * @Description 随机调度
 * @Author <font color='blue'>zhangcm</font>
 */
public class RandomScheduler {

    private static final Random RANDOM = new Random();

    private RandomScheduler() {
    }

    public static <T> T choose(List<T> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(RANDOM.nextInt(list.size()));
        }
        return null;
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }
}
