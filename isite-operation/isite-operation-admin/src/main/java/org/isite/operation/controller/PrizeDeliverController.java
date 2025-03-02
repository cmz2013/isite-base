package org.isite.operation.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.operation.po.PrizeDeliverPo;
import org.isite.operation.po.PrizeRecordPo;
import org.isite.operation.service.PrizeDeliverService;
import org.isite.operation.service.PrizeRecordService;
import org.isite.operation.support.dto.PrizeDeliverDto;
import org.isite.operation.support.vo.PrizeDeliver;
import org.isite.user.data.dto.ConsigneeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getEmployeeId;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.operation.converter.PrizeDeliverConverter.toPrizeDeliverPo;
import static org.isite.operation.converter.PrizeDeliverConverter.toPrizeDeliverSelectivePo;
import static org.isite.operation.support.constants.OperationUrls.URL_OPERATION;
import static org.springframework.beans.BeanUtils.copyProperties;

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
    @PutMapping(URL_MY + URL_OPERATION + "/prize/{prizeRecordId}/consignee")
    public Result<Integer> savePrizeDeliver(@PathVariable("prizeRecordId") Long prizeRecordId,
                                            @RequestBody @Validated(Add.class) ConsigneeDto consigneeDto) {
        PrizeRecordPo prizeRecordPo = prizeRecordService.get(prizeRecordId);
        isTrue(getUserId().equals(prizeRecordPo.getUserId()), new OverstepAccessError());
        PrizeDeliverPo prizeDeliverPo = prizeDeliverService.findOne(PrizeDeliverPo::getPrizeRecordId, prizeRecordId);
        if (null == prizeDeliverPo) {
            return toResult(prizeDeliverService.insert(toPrizeDeliverPo(prizeRecordId, consigneeDto)));
        } else {
            copyProperties(consigneeDto, prizeDeliverPo);
            return toResult(prizeDeliverService.updateById(prizeDeliverPo));
        }
    }

    /**
     * 负责人（员工）完成交付
     */
    @PutMapping(URL_OPERATION + "/prize/{prizeRecordId}/deliver")
    public Result<Integer> updatePrizeDeliver(
            @PathVariable("prizeRecordId") Long prizeRecordId, @Validated @RequestBody PrizeDeliverDto prizeDeliverDto) {
        return toResult(prizeDeliverService.updatePrizeDeliver(
                prizeRecordId, toPrizeDeliverSelectivePo(getEmployeeId(), prizeDeliverDto)));
    }

    /**
     * 用户查询奖品交付信息
     */
    @GetMapping(URL_MY + URL_OPERATION + "/prize/{prizeRecordId}/deliver")
    public Result<PrizeDeliver> getMyPrizeDeliver(@PathVariable("prizeRecordId") Long prizeRecordId) {
        PrizeRecordPo prizeRecordPo = prizeRecordService.get(prizeRecordId);
        isTrue(getUserId().equals(prizeRecordPo.getUserId()), new OverstepAccessError());
        return toResult(convert(prizeDeliverService.findOne(PrizeDeliverPo::getPrizeRecordId, prizeRecordId), PrizeDeliver::new));
    }

    /**
     * 后台管理员查询奖品交付信息
     */
    @GetMapping(URL_OPERATION + "/prize/{prizeRecordId}/deliver")
    public Result<PrizeDeliver> getPrizeDeliver(@PathVariable("prizeRecordId") Long prizeRecordId) {
        return toResult(convert(prizeDeliverService.findOne(
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
