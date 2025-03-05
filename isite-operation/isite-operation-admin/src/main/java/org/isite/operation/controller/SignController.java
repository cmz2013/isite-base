package org.isite.operation.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.commons.web.mq.Message;
import org.isite.commons.web.mq.Publisher;
import org.isite.operation.mq.SignProducer;
import org.isite.operation.service.SignLogService;
import org.isite.operation.support.constants.OperationConstants;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.vo.SignLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 每日签到
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class SignController extends BaseController {
    /**
     * 签到记录 Service
     */
    private SignLogService signLogService;

    /**
     * 完成每日签到
     */
    @PostMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/sign/log")
    @Publisher(messages = @Message(queues = OperationConstants.QUEUE_OPERATION_EVENT, producer = SignProducer.class))
    public Result<SignLog> saveSignLog() {
        return toResult(DataConverter.convert(signLogService.saveSignLog(TransmittableHeaders.getUserId()), SignLog::new));
    }

    /**
     * 用户查询签到信息
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/sign/log")
    public Result<SignLog> getSignLog() {
        return toResult(DataConverter.convert(signLogService.getLastSignLog(TransmittableHeaders.getUserId()), SignLog::new));
    }

    @Autowired
    public void setSignLogService(SignLogService signLogService) {
        this.signLogService = signLogService;
    }
}
