package org.isite.tenant.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.tenant.data.dto.EmployeeDto;
import org.isite.tenant.data.vo.Employee;
import org.isite.tenant.po.EmployeePo;
import org.isite.tenant.service.DepartmentService;
import org.isite.tenant.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.Converter.convert;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.EmployeeConverter.toEmployeePo;
import static org.isite.tenant.converter.EmployeeConverter.toEmployeeSelectivePo;
import static org.isite.tenant.data.constants.UrlConstants.GET_EMPLOYEE;
import static org.isite.tenant.data.constants.UrlConstants.URL_TENANT;
import static org.isite.user.client.UserAccessor.addUserIfAbsent;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class EmployeeController extends BaseController {

    private DepartmentService departmentService;
    private EmployeeService employeeService;

    @GetMapping(GET_EMPLOYEE)
    public Result<Employee> getEmployee(@PathVariable("id") long id) {
        EmployeePo employeePo = employeeService.get(id);
        isTrue(employeePo.getTenantId().equals(getTenantId()), new OverstepAccessError());
        return toResult(convert(employeePo, Employee::new));
    }

    @PostMapping(URL_TENANT + "/employee")
    public Result<Integer> addEmployee(@Validated(Add.class) @RequestBody EmployeeDto employeeDto) {
        int tenantId = getTenantId();
        isTrue(departmentService.get(employeeDto.getDeptId()).getTenantId().equals(tenantId), new OverstepAccessError());
        isFalse(employeeService.exists(toEmployeeSelectivePo(tenantId, employeeDto.getDomainAccount())),
                getMessage("employee.domainAccount.exists", "The domain account already exists"));
        long userId = addUserIfAbsent(employeeDto.getPhone());
        isFalse(employeeService.exists(toEmployeeSelectivePo(tenantId, userId)),
                getMessage("employee.exists", "The employee already exists"));
        return toResult(employeeService.insert(toEmployeePo(tenantId, employeeDto, userId)));
    }

    @PutMapping(URL_TENANT + "/employee")
    public Result<Integer> updateEmployee(@Validated(Update.class) @RequestBody EmployeeDto employeeDto) {
        int tenantId = getTenantId();
        EmployeePo employeePo = employeeService.get(employeeDto.getId());
        isTrue(employeePo.getTenantId().equals(tenantId), new OverstepAccessError());
        isTrue(employeePo.getDeptId().equals(employeeDto.getDeptId()) ||
                departmentService.get(employeeDto.getDeptId()).getTenantId().equals(tenantId), new OverstepAccessError());
        isTrue(employeePo.getDomainAccount().equals(employeeDto.getDomainAccount()) ||
                        !employeeService.exists(toEmployeeSelectivePo(tenantId, employeeDto.getDomainAccount())),
                getMessage("employee.domainAccount.exists", "The domain account already exists"));
        long userId = addUserIfAbsent(employeeDto.getPhone());
        isTrue(employeePo.getUserId().equals(userId) ||
                        !employeeService.exists(toEmployeeSelectivePo(tenantId, userId)),
                getMessage("employee.exists", "The employee already exists"));
        return toResult(employeeService.updateSelectiveById(toEmployeeSelectivePo(tenantId, employeeDto, userId)));
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
