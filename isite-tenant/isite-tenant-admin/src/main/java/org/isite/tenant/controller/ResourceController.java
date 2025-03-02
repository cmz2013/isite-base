package org.isite.tenant.controller;

import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.web.exception.OverstepAccessError;
import org.isite.commons.web.sign.Signed;
import org.isite.tenant.data.dto.ResourceDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.po.RoleResourcePo;
import org.isite.tenant.service.ResourceService;
import org.isite.tenant.service.RoleResourceService;
import org.isite.tenant.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.TreeConverter.toTree;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.lang.Assert.isTrue;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getTenantId;
import static org.isite.tenant.converter.ResourceConverter.toResourcePo;
import static org.isite.tenant.data.constants.TenantUrls.API_GET_RESOURCES;
import static org.isite.tenant.data.constants.TenantUrls.URL_TENANT;

/**
 * @Description 系统资源Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ResourceController extends BaseController {

    private ResourceService resourceService;
    private RoleService roleService;
    private RoleResourceService roleResourceService;

    /**
     * 内置用户登录时获取客户端所有资源
     */
    @Signed
    @GetMapping(API_GET_RESOURCES)
    public Result<List<Resource>> getResources(@RequestParam("clientId") String clientId) {
        return toResult(toTree(resourceService.findList(ResourcePo::getClientId, clientId),
                po -> convert(po, Resource::new)));
    }

    /**
     * 根据客户端ID和父节点ID查询资源。如果不是内置用户(tenantId = 0)，则只能查询租户自己的资源
     */
    @GetMapping(URL_TENANT + "/resources/{pid}")
    public Result<List<Resource>> findResources(@PathVariable("pid") Integer pid) {
        List<Integer> resourceIds = null;
        if (ZERO != getTenantId()) {
            resourceIds = roleResourceService.findList(RoleResourcePo::getRoleId, roleService.getAdminRole(
                    getTenantId()).getId()).stream().map(RoleResourcePo::getResourceId).collect(toList());
        }
        return toResult(toTree(resourceService.findResources(pid, resourceIds),
                po -> convert(po, Resource::new)));
    }

    /**
     * 系统内置用户添加资源信息
     */
    @PostMapping(URL_TENANT + "/resource")
    public Result<Integer> addResource(@RequestBody @Validated(Add.class) ResourceDto resourceDto) {
        isTrue(ZERO == getTenantId(), new OverstepAccessError());
        return toResult(resourceService.insert(toResourcePo(resourceDto)));
    }

    /**
     * 系统内置用户删除资源信息
     */
    @DeleteMapping(URL_TENANT + "/resource/{id}")
    public Result<Integer> deleteResource(@PathVariable("id") Integer id) {
        isTrue(ZERO == getTenantId(), new OverstepAccessError());
        isFalse(roleResourceService.exists(RoleResourcePo::getResourceId, id),
                "The resource is already in use and cannot be deleted");
        return toResult(resourceService.delete(id));
    }

    /**
     * 不推荐使用字段注入，通过setter方法注入，更符合面向对象的思想
     * 不是任意的方法注入，而是构造方法或setter方法，这种initOauthInterceptor(OauthInterceptor oauthInterceptor)自定义的方法是无法完成注入的。
     * 注解@Autowired是按照类型（byType）装配依赖对象，默认情况下它要求依赖对象必须存在，如果允许null值，可以设置它的required属性为false
     * 如果我们想使用按照名称（byName）来装配，可以结合@Qualifier注解一起使用
     */
    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setRoleResourceService(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }
}