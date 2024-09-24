package org.isite.tenant.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.security.data.vo.OauthClient;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RoleResourcePo;
import org.isite.tenant.service.ResourceService;
import org.isite.tenant.service.RoleResourceService;
import org.isite.tenant.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.security.client.EndpointAccessor.findOauthClients;
import static org.isite.tenant.data.constants.UrlConstants.GET_OAUTH_CLIENTS;

@RestController
public class EndpointController extends BaseController {

    private RoleService roleService;
    private RoleResourceService roleResourceService;
    private ResourceService resourceService;

    /**
     * 租户查询可以访问的客户端
     */
    @GetMapping(GET_OAUTH_CLIENTS)
    public Result<List<OauthClient>> findClients() {
        List<Integer> resourceIds = convert(roleResourceService.findList(RoleResourcePo::getRoleId,
                roleService.getAdminRole(getTenantId()).getId()), RoleResourcePo::getResourceId);
        return toResult(findOauthClients(convert(
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
