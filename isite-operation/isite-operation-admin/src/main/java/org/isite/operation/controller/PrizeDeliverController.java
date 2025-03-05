package org.isite.operation.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.operation.converter.PrizeDeliverConverter;
import org.isite.operation.po.PrizeDeliverPo;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeDeliverService;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.support.constants.OperationUrls;
import org.isite.operation.support.dto.PrizeDeliverDto;
import org.isite.operation.support.vo.PrizeDeliver;
import org.isite.user.data.dto.ConsigneeDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class PrizeDeliverController extends BaseController {
    private PrizeRecordService prizeRecordService;
    private PrizeDeliverService prizeDeliverService;

    /**
     * @Description 用户提交奖品收件人
     */
    @PutMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/prize/{prizeRecordId}/consignee")
    public Result<Integer> savePrizeDeliver(@PathVariable("prizeRecordId") Long prizeRecordId,
                                            @RequestBody @Validated(Add.class) ConsigneeDto consigneeDto) {
        PrizeRecordPo prizeRecordPo = prizeRecordService.get(prizeRecordId);
        Assert.isTrue(TransmittableHeaders.getUserId().equals(prizeRecordPo.getUserId()), new OverstepAccessError());
        PrizeDeliverPo prizeDeliverPo = prizeDeliverService.findOne(PrizeDeliverPo::getPrizeRecordId, prizeRecordId);
        if (null == prizeDeliverPo) {
            return toResult(prizeDeliverService.insert(PrizeDeliverConverter.toPrizeDeliverPo(prizeRecordId, consigneeDto)));
        } else {
            BeanUtils.copyProperties(consigneeDto, prizeDeliverPo);
            return toResult(prizeDeliverService.updateById(prizeDeliverPo));
        }
    }

    /**
     * 负责人（员工）完成交付
     */
    @PutMapping(OperationUrls.URL_OPERATION + "/prize/{prizeRecordId}/deliver")
    public Result<Integer> updatePrizeDeliver(
            @PathVariable("prizeRecordId") Long prizeRecordId, @Validated @RequestBody PrizeDeliverDto prizeDeliverDto) {
        return toResult(prizeDeliverService.updatePrizeDeliver(prizeRecordId,
                PrizeDeliverConverter.toPrizeDeliverSelectivePo(TransmittableHeaders.getEmployeeId(), prizeDeliverDto)));
    }

    /**
     * 用户查询奖品交付信息
     */
    @GetMapping(UrlConstants.URL_MY + OperationUrls.URL_OPERATION + "/prize/{prizeRecordId}/deliver")
    public Result<PrizeDeliver> getMyPrizeDeliver(@PathVariable("prizeRecordId") Long prizeRecordId) {
        PrizeRecordPo prizeRecordPo = prizeRecordService.get(prizeRecordId);
        Assert.isTrue(TransmittableHeaders.getUserId().equals(prizeRecordPo.getUserId()), new OverstepAccessError());
        return toResult(DataConverter.convert(prizeDeliverService.findOne(
                PrizeDeliverPo::getPrizeRecordId, prizeRecordId), PrizeDeliver::new));
    }

    /**
     * 后台管理员查询奖品交付信息
     */
    @GetMapping(OperationUrls.URL_OPERATION + "/prize/{prizeRecordId}/deliver")
    public Result<PrizeDeliver> getPrizeDeliver(@PathVariable("prizeRecordId") Long prizeRecordId) {
        return toResult(DataConverter.convert(prizeDeliverService.findOne(
                PrizeDeliverPo::getPrizeRecordId, prizeRecordId), PrizeDeliver::new));
    }

    @Autowired
    public void setPrizeRecordService(PrizeRecordService prizeRecordService) {
        this.prizeRecordService = prizeRecordService;
    }

    @Autowired
    public void setPrizeDeliverService(PrizeDeliverService prizeDeliverService) {
        this.prizeDeliverService = prizeDeliverService;
    }
}
