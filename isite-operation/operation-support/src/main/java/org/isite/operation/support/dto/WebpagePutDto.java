package org.isite.operation.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
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
     * 源码（thymeleaf模板页面）
     */
    @NotBlank
    private String code;
    /**
     * 用户终端类型
     */
    @NotNull
    private TerminalType terminalType;
}
