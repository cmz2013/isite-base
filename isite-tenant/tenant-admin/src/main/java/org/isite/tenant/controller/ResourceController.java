package org.isite.tenant.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.web.sign.Signature;
import org.isite.tenant.data.dto.ResourceDto;
import org.isite.tenant.data.vo.Resource;
import org.isite.tenant.po.ResourcePo;
import org.isite.tenant.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.jpa.converter.TreeConverter.toTree;
import static org.isite.tenant.data.constant.UrlConstants.API_GET_CLIENT_RESOURCES;
import static org.isite.tenant.data.constant.UrlConstants.URL_TENANT;

/**
 * @Description 系统资源Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ResourceController extends BaseController {

    private ResourceService resourceService;

    @Signature
    @GetMapping(API_GET_CLIENT_RESOURCES)
    public Result<List<Resource>> getResources(@PathVariable("clientId") String clientId) {
        return toResult(toTree(resourceService.findList(ResourcePo::getClientId, clientId),
                po -> convert(po, Resource::new)));
    }

    /**
     * 根据父节点ID，查询终端资源
     */
    @GetMapping(URL_TENANT + "/resources/{clientId}/{pid}")
    public Result<List<Resource>> findResources(
            @PathVariable("clientId") String clientId, @PathVariable("pid") Integer pid) {
        return toResult(toTree(resourceService.findResources(clientId, pid), po -> convert(po, Resource::new)));
    }

    /**
     * 根据ID查询资源信息
     */
    @GetMapping(URL_TENANT + "/resource/{id}")
    public Result<Resource> findById(@PathVariable("id") Integer id) {
        return toResult(convert(resourceService.get(id), Resource::new));
    }

    /**
     * 添加资源信息
     */
    @PostMapping(URL_TENANT + "/resource")
    public Result<Integer> addResource(
            @RequestBody @Validated(Add.class) ResourceDto resourceDto) {
        return toResult(resourceService.insert(convert(resourceDto, ResourcePo::new)));
    }

    /**
     * 删除资源信息
     */
    @DeleteMapping(URL_TENANT + "/resource/{id}")
    public Result<Integer> deleteResource(@PathVariable("id") Integer id) {
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

}