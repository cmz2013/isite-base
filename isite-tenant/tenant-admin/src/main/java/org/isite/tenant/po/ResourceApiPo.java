package org.isite.tenant.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Table(name = "resource_api")
@NoArgsConstructor
@AllArgsConstructor
public class ResourceApiPo extends Po<Integer> {
    /**
     * 资源ID
     */
    private Integer resourceId;
    /**
     * 数据接口ID
     */
    private Integer apiId;
}
