package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.sign.Signed;
import org.isite.tenant.data.dto.EmployeeDto;
import org.isite.tenant.data.dto.EmployeeGetDto;
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

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.EmployeeConverter.toEmployeePo;
import static org.isite.tenant.converter.EmployeeConverter.toEmployeeSelectivePo;
import static org.isite.tenant.data.constants.TenantUrls.API_GET_EMPLOYEE;
import static org.isite.tenant.data.constants.TenantUrls.GET_EMPLOYEE_LIST;
import static org.isite.tenant.data.constants.TenantUrls.URL_TENANT;
import static org.isite.user.client.UserAccessor.addPhoneIfAbsent;

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
    @GetMapping(GET_EMPLOYEE_LIST)
    public PageResult<Employee> findPage(PageRequest<EmployeeGetDto> request) {
        try (Page<EmployeePo> page = employeeService.findPage(toPageQuery(request, EmployeePo::new))) {
            return toPageResult(request, convert(page.getResult(), Employee::new), page.getTotal());
        }
    }

    @Signed
    @GetMapping(value = API_GET_EMPLOYEE)
    public Result<Employee> getEmployee(@PathVariable("employeeId") Long employeeId) {
        return toResult(convert(employeeService.get(employeeId), Employee::new));
    }

    @PostMapping(URL_TENANT + "/employee")
    public Result<Integer> addEmployee(@Validated(Add.class) @RequestBody EmployeeDto employeeDto) {
        int tenantId = getTenantId();
        isTrue(departmentService.get(employeeDto.getDeptId()).getTenantId().equals(tenantId), new OverstepAccessError());
        isFalse(employeeService.exists(toEmployeeSelectivePo(tenantId, employeeDto.getDomainAccount())),
                getMessage("employee.domainAccount.exists", "The domain account already exists"));
        long userId = addPhoneIfAbsent(employeeDto.getPhone());
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
        long userId = addPhoneIfAbsent(employeeDto.getPhone());
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
