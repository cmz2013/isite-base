package org.isite.user.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
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

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.converter.Converter.convert;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;
import static org.isite.user.converter.ConsigneeConverter.toConsigneePo;
import static org.isite.user.data.constants.UrlConstants.URL_USER;

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
    @GetMapping(URL_MY + URL_USER + "/consignee")
    public Result<Consignee> getConsignee() {
        return toResult(convert(consigneeService.getConsignee(getUserId()), Consignee::new));
    }

    /**
     * 查询用户所有收件人信息
     */
    @GetMapping(URL_MY + URL_USER + "/consignees")
    public Result<List<Consignee>> findList() {
        return toResult(convert(consigneeService.findList(ConsigneePo::getUserId, getUserId()), Consignee::new));
    }

    /**
     * 添加用户收件人
     */
    @PostMapping(URL_MY + URL_USER + "/consignee")
    public Result<Integer> addConsignee(@RequestBody @Validated(Add.class) ConsigneeDto consigneeDto) {
        return toResult(consigneeService.insert(toConsigneePo(consigneeDto)));
    }

    /**
     * 更新用户收件人
     */
    @PutMapping(URL_MY + URL_USER + "/consignee")
    public Result<Integer> editConsignee(
            @RequestBody @Validated(Update.class) ConsigneeDto consigneeDto) {
        isTrue(consigneeService.get(consigneeDto.getId()).getUserId().equals(getUserId()), new OverstepAccessError());
        return toResult(consigneeService.updateSelectiveById(convert(consigneeDto, ConsigneePo::new)));
    }

    /**
     * 删除收件人
     */
    @DeleteMapping(URL_MY + URL_USER + "/consignee/{consigneeId}")
    public Result<Integer> deleteConsignee(@PathVariable("consigneeId") Long consigneeId) {
        isTrue(consigneeService.get(consigneeId).getUserId().equals(getUserId()), new OverstepAccessError());
        return toResult(consigneeService.delete(consigneeId));
    }

    /**
     * 设置默认地址（有且只能有一个）
     */
    @PutMapping(URL_MY + URL_USER + "/consignee/{consigneeId}/defaults")
    public Result<Integer> setDefaults(@PathVariable("consigneeId") Long consigneeId) {
        return toResult(consigneeService.setDefaults(getUserId(), consigneeId));
    }
}
