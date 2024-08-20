package org.isite.commons.lang.schedule;

import java.util.List;
import java.util.Random;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @Description 随机调度
 * @Author <font color='blue'>zhangcm</font>
 */
public class RandomScheduler {

    private static final Random RANDOM = new Random();

    private RandomScheduler() {
    }

    public static <T> T choose(List<T> list) {
        if (isNotEmpty(list)) {
            return list.get(RANDOM.nextInt(list.size()));
        }
        return null;
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }
}
