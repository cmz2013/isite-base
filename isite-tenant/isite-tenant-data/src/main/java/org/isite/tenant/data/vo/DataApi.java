package org.isite.tenant.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.enums.HttpMethod;
import org.isite.commons.cloud.data.vo.Vo;

import java.util.Objects;
/**
 * @Description 数据接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DataApi extends Vo<Integer> {
    /**
     * 服务ID: spring.application.name
     */
    private String serviceId;
    /**
     * 接口路径，支持通配符
     */
    private String requestPath;
    /**
     * HTTP 请求方法
     */
    private HttpMethod method;
    /**
     * 备注
     */
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataApi dataApi = (DataApi) o;
        return serviceId.equals(dataApi.serviceId) &&
                method == dataApi.method &&
                requestPath.equals(dataApi.requestPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, method, requestPath);
    }
}