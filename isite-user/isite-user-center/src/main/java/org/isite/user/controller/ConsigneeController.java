package org.isite.user.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.constants.UrlConstants;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.user.converter.ConsigneeConverter;
import org.isite.user.data.constants.UserUrls;
import org.isite.user.data.dto.ConsigneeDto;
import org.isite.user.data.vo.Consignee;
import org.isite.user.po.ConsigneePo;
import org.isite.user.service.ConsigneeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ConsigneeController extends BaseController {
    private ConsigneeService consigneeService;

    @Autowired
    public void setConsigneeService(ConsigneeService consigneeService) {
        this.consigneeService = consigneeService;
    }

    /**
     * 查询收件人信息，优先返回默认收件人
     */
    @GetMapping(UrlConstants.URL_MY + UserUrls.URL_USER + "/consignee")
    public Result<Consignee> getConsignee() {
        return toResult(DataConverter.convert(consigneeService.getConsignee(
                TransmittableHeaders.getUserId()), Consignee::new));
    }

    /**
     * 查询用户所有收件人信息
     */
    @GetMapping(UrlConstants.URL_MY + UserUrls.URL_USER + "/consignees")
    public Result<List<Consignee>> findList() {
        return toResult(DataConverter.convert(consigneeService.findList(
                ConsigneePo::getUserId, TransmittableHeaders.getUserId()), Consignee::new));
    }

    /**
     * 添加用户收件人
     */
    @PostMapping(UrlConstants.URL_MY + UserUrls.URL_USER + "/consignee")
    public Result<Integer> addConsignee(@RequestBody @Validated(Add.class) ConsigneeDto consigneeDto) {
        return toResult(consigneeService.insert(ConsigneeConverter.toConsigneePo(consigneeDto)));
    }

    /**
     * 更新用户收件人
     */
    @PutMapping(UrlConstants.URL_MY + UserUrls.URL_USER + "/consignee")
    public Result<Integer> editConsignee(
            @RequestBody @Validated(Update.class) ConsigneeDto consigneeDto) {
        Assert.isTrue(consigneeService.get(consigneeDto.getId()).getUserId()
                .equals(TransmittableHeaders.getUserId()), new OverstepAccessError());
        return toResult(consigneeService.updateSelectiveById(DataConverter.convert(consigneeDto, ConsigneePo::new)));
    }

    /**
     * 删除收件人
     */
    @DeleteMapping(UrlConstants.URL_MY + UserUrls.URL_USER + "/consignee/{consigneeId}")
    public Result<Integer> deleteConsignee(@PathVariable("consigneeId") Long consigneeId) {
        Assert.isTrue(consigneeService.get(consigneeId).getUserId()
                .equals(TransmittableHeaders.getUserId()), new OverstepAccessError());
        return toResult(consigneeService.delete(consigneeId));
    }

    /**
     * 设置默认地址（有且只能有一个）
     */
    @PutMapping(UrlConstants.URL_MY + UserUrls.URL_USER + "/consignee/{consigneeId}/defaults")
    public Result<Integer> setDefaults(@PathVariable("consigneeId") Long consigneeId) {
        long userId = TransmittableHeaders.getUserId();
        Assert.isTrue(consigneeService.get(consigneeId).getUserId().equals(userId), new OverstepAccessError());
        return toResult(consigneeService.setDefaults(userId, consigneeId));
    }
}
