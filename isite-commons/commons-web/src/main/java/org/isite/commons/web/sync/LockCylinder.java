package org.isite.commons.web.sync;

import lombok.Getter;

/**
 * @Description 锁芯
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public class LockCylinder {
    /**
     * 锁名称（锁KEY）
     */
    private final String name;
    /**
     * 可重入次数。加锁时，在KEY的末尾追加当前进入次数
     */
    private final int reentry;

    public LockCylinder(String name, int reentry) {
        this.name = name;
        this.reentry = reentry;
    }
}
