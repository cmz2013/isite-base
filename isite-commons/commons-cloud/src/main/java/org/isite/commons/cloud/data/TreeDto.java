package org.isite.commons.cloud.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 树 DTO。pids由后端负责设置，接口不传参
 *
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TreeDto<I> extends Dto<I> {
    /**
     * 父节点ID（根节点为0）
     */
    private I pid;
}
