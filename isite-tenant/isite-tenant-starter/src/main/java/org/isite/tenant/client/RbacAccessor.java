package org.isite.tenant.client;

import org.isite.commons.cloud.utils.ApplicationContextUtils;
import org.isite.commons.cloud.utils.ResultUtils;
import org.isite.commons.web.feign.FeignClientFactory;
import org.isite.tenant.data.constants.TenantConstants;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
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
        FeignClientFactory feignClientFactory = ApplicationContextUtils.getBean(FeignClientFactory.class);
        RbacClient rbacClient = feignClientFactory.getFeignClient(RbacClient.class, TenantConstants.SERVICE_ID);
        return ResultUtils.getData(rbacClient.getRbac(loginDto, signPassword));
    }
}
