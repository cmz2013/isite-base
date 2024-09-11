package org.isite.tenant.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;

/**
 * @author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
//@AllArgsConstructor 不建议使用，因为字段顺序一旦调整，构造函数传参就会错误，如果类型匹配很容易忽略该错误
@Table(name = "resource_api")
public class ResourceApiPo extends Po<Integer> {
    /**
     * 资源ID
     */
    private Integer resourceId;
    /**
     * 数据接口ID
     */
    private Integer apiId;

    public ResourceApiPo() {
        super();
    }

    public ResourceApiPo(Integer resourceId, Integer apiId) {
        super();
        this.resourceId = resourceId;
        this.apiId = apiId;
    }
}
