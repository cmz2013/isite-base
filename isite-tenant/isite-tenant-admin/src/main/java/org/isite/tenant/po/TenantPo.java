package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 租户信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "tenant")
public class TenantPo extends Po<Integer> {
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 启用/停用
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private SwitchStatus status;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 到期时间
     */
    private Date expireTime;
}
