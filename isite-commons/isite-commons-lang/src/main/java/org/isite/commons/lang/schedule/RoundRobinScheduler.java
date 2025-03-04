package org.isite.commons.lang.schedule;

import org.apache.commons.collections4.CollectionUtils;
import org.isite.commons.lang.Constants;

import java.util.List;
/**
 * @Description 轮询调度
 * @Author <font color='blue'>zhangcm</font>
 */
public class RoundRobinScheduler {
    /**
     * 定义一个循环的值，如果大于list.size就从0开始
     */
    private int index = Constants.ZERO;

    public <T> T choose(List<T> list) {
        T result = null;
        if (CollectionUtils.isNotEmpty(list)) {
            if (index >= list.size()) {
                index = Constants.ZERO;
            }
            result = list.get(index);
            //轮询+1
            index ++;
        }
        return result;
    }
}
