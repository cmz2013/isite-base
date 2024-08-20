package org.isite.user.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @Description 收件人
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "consignee")
public class ConsigneePo extends Po<Long> {
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
