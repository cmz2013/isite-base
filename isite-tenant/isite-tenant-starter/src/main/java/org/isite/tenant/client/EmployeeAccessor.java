package org.isite.tenant.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.tenant.data.vo.Employee;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.cloud.utils.ResultUtils.getData;
import static org.isite.tenant.data.constants.TenantConstants.SERVICE_ID;

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
        EmployeeClient employeeClient = getBean(FeignClientFactory.class).getFeignClient(EmployeeClient.class, SERVICE_ID);
        return getData(employeeClient.getEmployee(employeeId, signPassword));
    }
}
