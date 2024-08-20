package org.isite.commons.web.sync;

import lombok.Getter;
import lombok.Setter;

/**
 * 锁柱
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class LockCylinder {
    /**
     * 锁名称（锁KEY）
     */
    private String name;
    /**
     * 可重入次数。加锁时，在KEY的末尾追加当前进入次数
     */
    private int reentry;

    public LockCylinder(String name, int reentry) {
        this.name = name;
        this.reentry = reentry;
    }
}
