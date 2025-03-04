package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.commons.web.sign.Signed;
import org.isite.tenant.converter.EmployeeConverter;
import org.isite.tenant.data.constants.TenantUrls;
import org.isite.tenant.data.dto.EmployeeDto;
import org.isite.tenant.data.dto.EmployeeGetDto;
import org.isite.tenant.data.vo.Employee;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.service.DepartmentService;
import org.isite.tenant.service.EmployeeService;
import org.isite.user.client.UserAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class EmployeeController extends BaseController {
    private EmployeeService employeeService;
    private DepartmentService departmentService;

    /**
     * 企业管理员查询员工信息
     */
    @GetMapping(TenantUrls.GET_EMPLOYEE_LIST)
    public PageResult<Employee> findPage(PageRequest<EmployeeGetDto> request) {
        try (Page<EmployeePo> page = employeeService.findPage(PageQueryConverter.toPageQuery(request, EmployeePo::new))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), Employee::new), page.getTotal());
        }
    }

    @Signed
    @GetMapping(value = TenantUrls.API_GET_EMPLOYEE)
    public Result<Employee> getEmployee(@PathVariable("employeeId") Long employeeId) {
        return toResult(DataConverter.convert(employeeService.get(employeeId), Employee::new));
    }

    @PostMapping(TenantUrls.URL_TENANT + "/employee")
    public Result<Integer> addEmployee(@Validated(Add.class) @RequestBody EmployeeDto employeeDto) {
        int tenantId = TransmittableHeaders.getTenantId();
        Assert.isTrue(departmentService.get(employeeDto.getDeptId()).getTenantId().equals(tenantId), new OverstepAccessError());
        Assert.isFalse(employeeService.exists(EmployeePo::getDomainAccount, employeeDto.getDomainAccount()),
                MessageUtils.getMessage("employee.domainAccount.exists", "the domain account already exists"));
        long userId = UserAccessor.addPhoneIfAbsent(employeeDto.getPhone());
        Assert.isFalse(employeeService.exists(EmployeeConverter.toEmployeeSelectivePo(tenantId, userId)),
                MessageUtils.getMessage("employee.exists", "the employee already exists"));
        return toResult(employeeService.insert(EmployeeConverter.toEmployeePo(tenantId, employeeDto, userId)));
    }

    @PutMapping(TenantUrls.URL_TENANT + "/employee")
    public Result<Integer> updateEmployee(@Validated(Update.class) @RequestBody EmployeeDto employeeDto) {
        int tenantId = TransmittableHeaders.getTenantId();
        EmployeePo employeePo = employeeService.get(employeeDto.getId());
        Assert.isTrue(employeePo.getTenantId().equals(tenantId), new OverstepAccessError());
        Assert.isTrue(employeePo.getDeptId().equals(employeeDto.getDeptId()) ||
                departmentService.get(employeeDto.getDeptId()).getTenantId().equals(tenantId), new OverstepAccessError());
        Assert.isFalse(employeeService.exists(EmployeePo::getDomainAccount, employeeDto.getDomainAccount(), employeePo.getId()),
                MessageUtils.getMessage("employee.domainAccount.exists", "the domain account already exists"));
        long userId = UserAccessor.addPhoneIfAbsent(employeeDto.getPhone());
        Assert.isFalse(employeeService.exists(EmployeeConverter.toEmployeeSelectivePo(tenantId, userId), employeePo.getId()),
                MessageUtils.getMessage("employee.exists", "the employee already exists"));
        return toResult(employeeService.updateSelectiveById(EmployeeConverter.toEmployeeSelectivePo(tenantId, employeeDto, userId)));
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
}
