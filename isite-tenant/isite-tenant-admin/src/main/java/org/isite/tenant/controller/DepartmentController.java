package org.isite.tenant.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.TreeConverter;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.tenant.converter.DeptConverter;
import org.isite.tenant.data.constants.TenantUrls;
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
import java.util.stream.Collectors;
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

    @GetMapping(TenantUrls.URL_TENANT + "/departments")
    public Result<List<Department>> findDepts() {
        int tenantId = TransmittableHeaders.getTenantId();
        return toResult(TreeConverter.toTree(departmentService.findList(DepartmentPo::getTenantId, tenantId),
                departmentPo -> DataConverter.convert(departmentPo, Department::new)));
    }

    @PostMapping(TenantUrls.URL_TENANT + "/department")
    public Result<Integer> addDept(@RequestBody @Validated(Add.class) DepartmentDto departmentDto) {
        int tenantId = TransmittableHeaders.getTenantId();
        Assert.isFalse(departmentService.exists(DeptConverter.toDeptSelectivePo(tenantId, departmentDto.getDepartmentName())),
                MessageUtils.getMessage("dept.exists", "the department already exists"));
        return toResult(departmentService.insert(DeptConverter.toDeptPo(tenantId, departmentDto)));
    }

    @PutMapping(TenantUrls.URL_TENANT + "/department")
    public Result<Integer> updateDept(@RequestBody @Validated(Update.class) DepartmentDto departmentDto) {
        int tenantId = TransmittableHeaders.getTenantId();
        Assert.isTrue(departmentService.get(departmentDto.getId()).getTenantId().equals(tenantId), new OverstepAccessError());
        Assert.isFalse(departmentService.exists(DeptConverter.toDeptSelectivePo(tenantId, departmentDto.getDepartmentName()), departmentDto.getId()),
                MessageUtils.getMessage("dept.exists", "the department already exists"));
        return toResult(departmentService.updateSelectiveById(DeptConverter.toDeptSelectivePo(departmentDto)));
    }

    @DeleteMapping(TenantUrls.URL_TENANT + "/department/{id}")
    public Result<Integer> deleteDept(@PathVariable("id") Integer id) {
        DepartmentPo departmentPo = departmentService.get(id);
        Assert.isTrue(departmentPo.getTenantId().equals(TransmittableHeaders.getTenantId()), new OverstepAccessError());
        List<Integer> deptIds = departmentService.findLikePids(departmentService.getPids(departmentPo)).stream()
                .map(DepartmentPo::getId).collect(Collectors.toList());
        deptIds.add(id);
        Assert.isTrue(employeeService.countIn(EmployeePo::getDeptId, deptIds) == Constants.ZERO,
                MessageUtils.getMessage("dept.notDeleted.employees","there are employees in the department and cannot be deleted"));
        return toResult(departmentService.delete(id));
    }
}
