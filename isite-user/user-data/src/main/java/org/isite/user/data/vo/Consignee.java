package org.isite.user.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.vo.Vo;

/**
 * @Description 收件人
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Consignee extends Vo<Long> {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 地区编码
     */
    private String regionCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 默认地址
     */
    private Boolean defaults;
}
