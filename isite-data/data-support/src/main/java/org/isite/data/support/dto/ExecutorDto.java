package org.isite.data.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;

import javax.validation.constraints.NotNull;

/**
 * @Description 执行器（使用注册中心的内部服务）
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class ExecutorDto extends Dto<String> {
    /**
     * 执行器编码
     */
    @NotNull
    private String appCode;
    /**
     * 执行器名称
     */
    @NotNull
    private String appName;
    /**
     * 执行器请求管理平台接口的签名私钥
     */
    @NotNull
    private String secret;
}
