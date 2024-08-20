package org.isite.data.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mongo.data.Po;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description 执行器PO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Document(collection = "executor")
public class ExecutorPo extends Po<String> {
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
