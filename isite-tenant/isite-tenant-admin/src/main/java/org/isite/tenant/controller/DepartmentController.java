package org.isite.tenant.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.tenant.data.dto.DepartmentDto;
import org.isite.tenant.data.vo.Department;
import org.isite.tenant.po.DepartmentPo;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.service.DepartmentService;
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
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.TreeConverter.toTree;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.DeptConverter.toDeptPo;
import static org.isite.tenant.converter.DeptConverter.toDeptSelectivePo;
import static org.isite.tenant.data.constants.UrlConstants.URL_TENANT;

/**
 * @Description 部门 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DepartmentController extends BaseController {

    private DepartmentService departmentService;
    private EmployeeService employeeService;

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(URL_TENANT + "/departments")
    public Result<List<Department>> findDepts() {
        List<DepartmentPo> departmentPos = departmentService.findList(DepartmentPo::getTenantId, getTenantId());
        return toResult(toTree(departmentPos, departmentPo -> convert(departmentPo, Department::new)));
    }

    @PostMapping(URL_TENANT + "/department")
    public Result<Integer> addDept(@RequestBody @Validated(Add.class) DepartmentDto departmentDto) {
        int tenantId = getTenantId();
        isFalse(departmentService.exists(toDeptSelectivePo(tenantId, departmentDto.getDepartmentName())),
                getMessage("dept.exists", "This department already exists"));
        return toResult(departmentService.insert(toDeptPo(tenantId, departmentDto)));
    }

    @PutMapping(URL_TENANT + "/department")
    public Result<Integer> updateDept(@RequestBody @Validated(Update.class) DepartmentDto departmentDto) {
        int tenantId = getTenantId();
        isTrue(departmentService.get(departmentDto.getId()).getTenantId().equals(tenantId), new OverstepAccessError());
        isFalse(departmentService.exists(toDeptSelectivePo(tenantId, departmentDto.getDepartmentName()), departmentDto.getId()),
                getMessage("dept.exists", "This department already exists"));
        return toResult(departmentService.updateSelectiveById(toDeptSelectivePo(departmentDto)));
    }

    @DeleteMapping(URL_TENANT + "/department/{id}")
    public Result<Integer> deleteDept(@PathVariable("id") Integer id) {
        DepartmentPo departmentPo = departmentService.get(id);
        isTrue(departmentPo.getTenantId().equals(getTenantId()), new OverstepAccessError());
        List<Integer> deptIds = departmentService.findLikePids(departmentService.getPids(departmentPo)).stream()
                .map(DepartmentPo::getId).collect(toList());
        deptIds.add(id);
        isTrue(employeeService.countIn(EmployeePo::getDeptId, deptIds) == ZERO,
                getMessage("dept.notDeleted.employees",
                "There are employees in the department and cannot be deleted"));
        return toResult(departmentService.delete(id));
    }
}
