package org.isite.tenant.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.data.vo.Employee;
/**
 * @Description EmployeeClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class EmployeeAccessor {

    private EmployeeAccessor() {
    }

    /**
     * 查询员工信息
     */
    public static Employee getEmployee(long employeeId, String signPassword) {
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        EmployeeClient employeeClient = feignClientFactory.getFeignClient(EmployeeClient.class, TenantConstants.SERVICE_ID);
        return ResultUtils.getData(employeeClient.getEmployee(employeeId, signPassword));
    }
}
