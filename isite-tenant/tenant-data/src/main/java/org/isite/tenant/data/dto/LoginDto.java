package org.isite.tenant.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 员工登录检索授权信息
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    /**
     * 租户ID
     */
    @NotNull
    private Integer tenantId;
    /**
     * 用户ID
     */
    @NotNull
    private Long userId;
    /**
     * 用户客户端ID
     */
    @NotBlank
    private String clientId;

    public LoginDto(Long userId, String clientId) {
        this.userId = userId;
        this.clientId = clientId;
    }

    public LoginDto(Integer tenantId, Long userId, String clientId) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.clientId = clientId;
    }
}
