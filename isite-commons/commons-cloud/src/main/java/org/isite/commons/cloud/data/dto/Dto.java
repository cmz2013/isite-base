package org.isite.commons.cloud.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * @Description DTO父类，用于Controller接口传参
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Dto<I> implements Serializable {
    /**
     * @Description 唯一标识，在执行指定操作时，SpringMVC自动完成参数绑定校验
     */
    @NotNull(groups = {Default.class, Update.class})
    private I id;
}
