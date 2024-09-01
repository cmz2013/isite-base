package org.isite.operation.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.enums.TerminalType;
import org.isite.commons.lang.data.Vo;

/**
 * @Description 活动网页模板
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Webpage extends Vo<Integer> {
    /**
     * 运营活动id
     */
    private Integer activityId;
    /**
     * 用户终端类型
     */
    private TerminalType terminalType;
    /**
     * 源码
     */
    private String code;
}
