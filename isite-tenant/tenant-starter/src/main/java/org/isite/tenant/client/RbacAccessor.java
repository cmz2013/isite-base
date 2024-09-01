package org.isite.tenant.client;

import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;

import static org.isite.commons.cloud.utils.ApplicationContextUtils.getBean;
import static org.isite.commons.lang.utils.ResultUtils.getData;
import static org.isite.tenant.data.constants.TenantConstants.SERVICE_ID;

/**
 * @Description RbacClient 辅助类
 * @Author <font color='blue'>zhangcm</font>
 */
public class RbacAccessor {

    private RbacAccessor() {
    }

    /**
     * @Description 检索员工登录授权信息
     */
    public static Rbac getRbac(LoginDto loginDto, String signPassword) {
        RbacClient rbacClient = getBean(FeignClientFactory.class).getFeignClient(RbacClient.class, SERVICE_ID);
        return getData(rbacClient.getRbac(loginDto, signPassword));
    }
}
