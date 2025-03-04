package org.isite.tenant.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.security.client.EndpointAccessor;
import org.isite.security.data.vo.OauthClient;
import org.isite.tenant.data.constants.TenantUrls;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RoleResourcePo;
import org.isite.tenant.service.ResourceService;
import org.isite.tenant.service.RoleResourceService;
import org.isite.tenant.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class EndpointController extends BaseController {
    private RoleService roleService;
    private RoleResourceService roleResourceService;
    private ResourceService resourceService;

    /**
     * 租户查询可以访问的客户端
     */
    @GetMapping(TenantUrls.GET_OAUTH_CLIENTS)
    public Result<List<OauthClient>> findClients() {
        List<Integer> resourceIds = DataConverter.convert(roleResourceService.findList(RoleResourcePo::getRoleId,
                roleService.getAdminRole(TransmittableHeaders.getTenantId()).getId()), RoleResourcePo::getResourceId);
        return toResult(EndpointAccessor.findOauthClients(DataConverter.convert(
                resourceService.findIn(ResourcePo::getId, resourceIds), ResourcePo::getClientId)));
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
}
