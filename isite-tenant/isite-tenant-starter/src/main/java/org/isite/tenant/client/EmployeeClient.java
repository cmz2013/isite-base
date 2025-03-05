package org.isite.tenant.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.feign.SignInterceptor;
import org.isite.tenant.data.constants.TenantUrls;
import org.isite.tenant.data.vo.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "resourceClient", value = SERVICE_ID)
public interface EmployeeClient {

    @GetMapping(value = TenantUrls.API_GET_EMPLOYEE)
    Result<Employee> getEmployee(
            @PathVariable("employeeId") long employeeId,
            @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);
}
