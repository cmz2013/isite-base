package org.isite.data.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DataLog extends Vo<String> {

    private String apiId;
    /**
     * 执行器编码
     */
    private String appCode;
    /**
     * 接口参数
     */
    private String reqData;
    /**
     * 接口返回数据
     */
    private String repData;
    /**
     * 接口执行结果
     */
    private Boolean status;

    private String remark;
}
