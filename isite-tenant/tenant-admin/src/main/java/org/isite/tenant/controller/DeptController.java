package org.isite.tenant.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.tenant.data.dto.DeptDto;
import org.isite.tenant.data.vo.Dept;
import org.isite.tenant.po.DeptPo;
import org.isite.tenant.service.DeptService;
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

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.DeptConverter.toDeptPo;
import static org.isite.tenant.data.constant.UrlConstants.URL_TENANT;

/**
 * @Description 组织机构 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DeptController extends BaseController {

    private DeptService deptService;

    @Autowired
    public void setDeptService(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping(URL_TENANT + "/depts")
    public Result<List<Dept>> findDepts() {
        List<DeptPo> deptPos = deptService.findList(DeptPo::getTenantId, getTenantId());
        return toResult(convert(deptPos, Dept::new));
    }

    @GetMapping(URL_TENANT + "/dept/{id}")
    public Result<Dept> findById(@PathVariable("id") Integer id) {
        return toResult(convert(deptService.get(id), Dept::new));
    }

    @PostMapping(URL_TENANT + "/dept")
    public Result<Integer> addDept(@RequestBody @Validated(Add.class) DeptDto deptDto) {
        return toResult(deptService.insert(toDeptPo(deptDto)));
    }

    @PutMapping(URL_TENANT + "/dept")
    public Result<Integer> updateDept(@RequestBody @Validated(Update.class) DeptDto deptDto) {
        return toResult(deptService.updateById(toDeptPo(deptDto)));
    }

    @DeleteMapping(URL_TENANT + "/dept/{id}")
    public Result<Integer> deleteDept(@PathVariable("id") Integer id) {
        return toResult(deptService.delete(id));
    }
}
