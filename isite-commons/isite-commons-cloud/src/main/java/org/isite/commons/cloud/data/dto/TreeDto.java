package org.isite.commons.cloud.data.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description 树 DTO。pids由后端负责设置，接口不传参
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class TreeDto<I> extends Dto<I> {
    /**
     * 父节点ID（根节点为0）
     */
    private I pid;
}
