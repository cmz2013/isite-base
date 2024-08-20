package org.isite.commons.web.sync;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;
import static org.isite.commons.lang.data.Constants.ZERO;

/**
 * @Description 忙时等待定时器。获取不到锁时，允许短时间的忙等待
 * 1)如果长时间的排队等待锁，可以在前端实现：前端页面周期循环尝试请求接口，用户随时可以取消排队等待。
 * 如果后端锁匠负责排队处理，很容易导致接口请求超时异常；而且，长时间的占用和浪费请求线程资源，会降低服务器的并发处理能力，即吞吐率。
 * 2)短时间的忙等待需要配置，默认不等待，即并发冲突时立即中断请求
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public class BusyTimer {
    /**
     * 忙时等待时间（毫秒）。默认不等待，即并发冲突时立即中断请求
     */
    private final long waiting;
    /**
     * 忙时重试次数，waiting > 0 时该配置生效
     */
    private int retry;

    public BusyTimer(long waiting, int retry) {
        this.waiting = waiting;
        this.retry = retry;
    }

    @SneakyThrows
    public boolean run() {
        if (this.waiting > ZERO && this.retry > ZERO) {
            sleep(waiting);
            this.retry--;
            return false;
        }
        return true;
    }
}
