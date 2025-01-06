package org.isite.commons.lang.schedule;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @description 轮询调度
 * @author <font color='blue'>zhangcm</font>
 */
public class RoundRobinScheduler {
    /**
     * 定义一个循环的值，如果大于list.size就从0开始
     */
    private Integer index = ZERO;

    public <T> T choose(List<T> list) {
        T result = null;
        if (isNotEmpty(list)) {
            if (index >= list.size()) {
                index = ZERO;
            }
            result = list.get(index);
            //轮询+1
            index ++;
        }
        return result;
    }
}
