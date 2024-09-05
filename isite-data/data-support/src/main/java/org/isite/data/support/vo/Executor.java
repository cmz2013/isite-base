package org.isite.data.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;

/**
 * @Description 执行器（使用注册中心的内部服务）
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Executor extends Vo<String> {
    /**
     * 执行器编码
     */
    private String appCode;
    /**
     * 执行器名称
     */
    private String appName;
    /**
     * 执行器请求管理平台接口的签名私钥
     */
    private String secret;
}
