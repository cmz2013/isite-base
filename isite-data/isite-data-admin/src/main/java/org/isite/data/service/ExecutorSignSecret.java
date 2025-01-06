package org.isite.data.service;

import org.isite.commons.web.sign.SignSecret;
import org.isite.data.po.ExecutorPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 执行器接口签名使用的秘钥
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class ExecutorSignSecret extends SignSecret {
    private ExecutorService executorService;

    @Override
    public String password(String appCode) {
        return executorService.findOne(ExecutorPo::getAppCode, appCode).getSecret();
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
