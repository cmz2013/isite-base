package org.isite.tenant.client;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.feign.SignInterceptor;
import org.isite.tenant.data.constants.TenantUrls;
import org.isite.tenant.data.dto.LoginDto;
import org.isite.tenant.data.vo.Rbac;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
//@FeignClient(contextId = "rbacClient", value = SERVICE_ID)
public interface RbacClient {
    /**
     * @Description 检索员工登录授权信息
     * feign的get方式默认不能解析对象，只支持基本类型参数，对象类型自动转POST请求。
     * 使用openfeign提供的@SpringQueryMap可以将对象属性转为url中的查询参数
     */
    @GetMapping(value = TenantUrls.API_GET_EMPLOYEE_RBAC)
    Result<Rbac> getRbac(@SpringQueryMap LoginDto loginDto,
                         @RequestHeader(SignInterceptor.FEIGN_SIGN_PASSWORD) String signPassword);
}
