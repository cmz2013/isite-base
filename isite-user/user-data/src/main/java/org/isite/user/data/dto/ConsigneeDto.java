package org.isite.user.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;

import javax.validation.constraints.NotBlank;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ConsigneeDto extends Dto<Long> {
    /**
     * 用户名
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String userName;
    /**
     * 联系电话
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String phone;
    /**
     * 地区编码
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String regionCode;
    /**
     * 详细地址
     */
    @NotBlank(groups = {Add.class, Update.class})
    private String address;
}
