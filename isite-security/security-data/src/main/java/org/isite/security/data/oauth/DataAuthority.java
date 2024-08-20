package org.isite.security.data.oauth;

import lombok.Getter;
import org.isite.commons.lang.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;

import static java.util.Objects.hash;
import static org.isite.commons.lang.Assert.notBlank;

/**
 * @Description 数据接口权限
 * @Author <font color='blue'>zhangcm</font>
 */
public class DataAuthority implements GrantedAuthority {
    /**
     * 如果method为空，接口匹配时不区分http方法。method不区分大小写
     */
    @Getter
    private final HttpMethod method;
    /**
     * 接口路径
     */
    private final String requestPath;

    public DataAuthority(HttpMethod method, String requestPath) {
        notBlank(requestPath, "requestPath is required");
        this.method = method;
        this.requestPath = requestPath;
    }

    @Override
    public String getAuthority() {
        return this.requestPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DataAuthority) {
            DataAuthority that = (DataAuthority) o;
            return requestPath.equals(that.requestPath) && (method == that.method);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash(method, requestPath);
    }
}