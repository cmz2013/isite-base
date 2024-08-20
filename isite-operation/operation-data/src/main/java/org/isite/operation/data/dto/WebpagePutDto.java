package org.isite.operation.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.enums.TerminalType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 活动ID禁止更新
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class WebpagePutDto extends Dto<Integer> {
    /**
     * 用户终端类型
     */
    @NotNull(groups = Update.class)
    private TerminalType terminalType;
    /**
     * 源码（thymeleaf模板页面）
     */
    @NotBlank(groups = Update.class)
    private String code;
}
