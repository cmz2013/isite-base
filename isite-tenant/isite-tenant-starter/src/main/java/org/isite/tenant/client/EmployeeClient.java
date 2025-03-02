package org.isite.tenant.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.tenant.data.vo.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.isite.commons.web.feign.SignInterceptor.FEIGN_SIGN_PASSWORD;
import static org.isite.tenant.data.constants.TenantUrls.API_GET_EMPLOYEE;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "resourceClient", value = SERVICE_ID)
public interface EmployeeClient {

    @GetMapping(value = API_GET_EMPLOYEE)
    Result<Employee> getEmployee(
            @PathVariable("employeeId") long employeeId,
            @RequestHeader(FEIGN_SIGN_PASSWORD) String signPassword);
}
