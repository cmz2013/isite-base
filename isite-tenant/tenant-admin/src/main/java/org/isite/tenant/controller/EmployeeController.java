package org.isite.tenant.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.tenant.data.vo.Employee;
import org.isite.tenant.service.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.tenant.data.constant.UrlConstants.GET_EMPLOYEE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class EmployeeController extends BaseController {

    private EmployeeService employeeService;

    @GetMapping(GET_EMPLOYEE)
    public Result<Employee> getEmployee(@PathVariable("id") long id) {
        return toResult(convert(employeeService.get(id), Employee::new));
    }
}
