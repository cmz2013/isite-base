package org.isite.security.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.Constants;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * @Description 客户端配置
 * @Author <font color="blue">zhangcm</font>
 */
@Setter
public class ClientProperties implements Serializable {
    /**
     * 客户端ID
     */
    @Getter
    private String clientId;
    /**
     * 客户端名称
     */
    @Getter
    private String clientName;
    /**
     * 设置access_token有效时间，秒
     */
    @Getter
    private Integer accessTokenValidity;
    /**
     * 设置refresh_token无效时间，秒
     */
    @Getter
    private Integer refreshTokenValidity;
    /**
     * client_secret 配置从 Spring Security 5.0开始必须是加密后的密码(new BCryptPasswordEncoder().encode)
     */
    @Getter
    private String secret;
    /**
     * 允许的授权范围，逗号分隔。
     */
    private String scopes;
    /**
     * 授权类型，逗号分隔
     */
    private String grantTypes;
    /**
     * 客户端的重定向URI
     */
    private String registeredRedirectUris;
    /**
     * token续签
     */
    @Getter
    private Boolean renewal;

    public List<String> getGrantTypes() {
        if (StringUtils.isNotBlank(this.grantTypes)) {
            return Arrays.asList(this.grantTypes.split(Constants.COMMA));
        }
        /*
         * Collections.EMPTY_LIST返回的是一个空的List。
         * Collections.EMPTY_LIST返回的这个空的List是不能进行添加元素这类操作的。
         *
         * 为什么需要空的List呢？
         * 有时候我们在函数中需要返回一个List，但是这个List是空的，如果我们直接返回null的话，
         * 调用者还需要进行null的判断，所以一般建议返回一个空的List。这时候你有可能会说，
         * 我直接返回一个new ArrayList()呗，但是new ArrayList()在初始化时会占用一定的资源，
         * 所以在这种场景下，还是建议返回Collections.EMPTY_LIST。
         *
         * Collections.emptyList()返回的也是一个空的List，它与Collections.EMPTY_LIST的唯一区别是，
         * Collections.emptyList()支持泛型，所以在需要泛型的时候，可以使用Collections.emptyList()
         */
        return Collections.emptyList();
    }

    public Set<String> getRegisteredRedirectUri() {
        if (StringUtils.isNotBlank(this.registeredRedirectUris)) {
            return new HashSet<>(Arrays.asList(this.registeredRedirectUris.split(Constants.COMMA)));
        }
        return Collections.emptySet();
    }

    public List<String> getScopes() {
        if (StringUtils.isNotBlank(this.scopes)) {
            return Arrays.asList(this.scopes.split(Constants.COMMA));
        }
        return Collections.emptyList();
    }
}
