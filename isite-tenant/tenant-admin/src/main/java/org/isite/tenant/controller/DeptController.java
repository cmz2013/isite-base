package org.isite.tenant.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.tenant.data.dto.DeptDto;
import org.isite.tenant.data.vo.Dept;
import org.isite.tenant.po.DeptPo;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.service.DeptService;
import org.isite.tenant.service.EmployeeService;
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

import static java.util.stream.Collectors.toList;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.jpa.converter.TreeConverter.toTree;
import static org.isite.tenant.converter.DeptConverter.toDeptPo;
import static org.isite.tenant.converter.DeptConverter.toDeptSelectivePo;
import static org.isite.tenant.data.constant.UrlConstants.URL_TENANT;

/**
 * @Description 部门 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DeptController extends BaseController {

    private DeptService deptService;
    private EmployeeService employeeService;

    @Autowired
    public void setDeptService(DeptService deptService) {
        this.deptService = deptService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(URL_TENANT + "/depts")
    public Result<List<Dept>> findDepts() {
        List<DeptPo> deptPos = deptService.findList(DeptPo::getTenantId, getTenantId());
        return toResult(toTree(deptPos, deptPo -> convert(deptPo, Dept::new)));
    }

    @PostMapping(URL_TENANT + "/dept")
    public Result<Integer> addDept(@RequestBody @Validated(Add.class) DeptDto deptDto) {
        return toResult(deptService.insert(toDeptPo(deptDto)));
    }

    @PutMapping(URL_TENANT + "/dept")
    public Result<Integer> updateDept(@RequestBody @Validated(Update.class) DeptDto deptDto) {
        isTrue(deptService.get(deptDto.getId()).getTenantId().equals(getTenantId()), new OverstepAccessError());
        return toResult(deptService.updateSelectiveById(toDeptSelectivePo(deptDto)));
    }

    @DeleteMapping(URL_TENANT + "/dept/{id}")
    public Result<Integer> deleteDept(@PathVariable("id") Integer id) {
        DeptPo deptPo = deptService.get(id);
        isTrue(deptPo.getTenantId().equals(getTenantId()), new OverstepAccessError());
        List<Integer> deptIds = deptService.findLikePids(deptService.getPids(deptPo)).stream()
                .map(DeptPo::getId).collect(toList());
        deptIds.add(id);
        isTrue(employeeService.countIn(EmployeePo::getDeptId, deptIds) == ZERO, getMessage("dept.notDeleted",
                "There are employees in the department and cannot be deleted"));
        return toResult(deptService.delete(id));
    }
}
