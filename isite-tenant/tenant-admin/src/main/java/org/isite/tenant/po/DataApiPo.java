package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.http.HttpMethod;
import org.isite.mybatis.data.Po;
import org.isite.mybatis.type.EnumTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Table;

/**
 * @Description 需要授权的数据接口（用户登录以后，如果没有授权不能访问）
 * /api/**：api打头的接口不校验token和接口权限
 * /my/**: my打头的接口不校验接口权限（当前用户只能访问自己的数据），但需要校验token才可以访问
 * /public/**: 公共接口不校验接口权限（所有用户都能访问的数据），但需要校验token才可以访问
 * 这三种接口不需要在rbac data_api表中进行配置
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "data_api")
public class DataApiPo extends Po<Integer> {
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 接口路径，支持通配符
     */
    private String requestPath;
    /**
     * http method,不区分大小写
     */
    @ColumnType(typeHandler = EnumTypeHandler.class)
    private HttpMethod method;
    /**
     * 备注
     */
    private String remark;
}
