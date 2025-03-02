package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.enums.ActiveStatus;
import org.isite.commons.web.controller.BaseController;
import org.isite.tenant.data.dto.TenantDto;
import org.isite.tenant.data.dto.TenantGetDto;
import org.isite.tenant.data.vo.Tenant;
import org.isite.tenant.po.TenantPo;
import org.isite.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.tenant.converter.TenantConverter.toTenantPo;
import static org.isite.tenant.converter.TenantConverter.toTenantSelectivePo;
import static org.isite.tenant.data.constants.TenantUrls.PUT_TENANT_STATUS;
import static org.isite.tenant.data.constants.TenantUrls.URL_TENANT;
import static org.isite.user.client.UserAccessor.addPhoneIfAbsent;

/**
 * @Description 租户信息 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TenantController extends BaseController {

    private TenantService tenantService;

    @GetMapping(URL_TENANT + "list")
    public PageResult<Tenant> findPage(PageRequest<TenantGetDto> request) {
        try (Page<TenantPo> page = tenantService.findPage(toPageQuery(request, TenantPo::new))) {
            return toPageResult(request, convert(page.getResult(), Tenant::new), page.getTotal());
        }
    }

    @GetMapping(URL_TENANT + "/{id}")
    public Result<Tenant> findById(@PathVariable("id") Integer id) {
        return toResult(convert(tenantService.get(id), Tenant::new));
    }

    /**
     * @Description 添加租户。
     * SpringMVC框架提交参数list时，默认只能接收到256个数据，可以单独Controller或者全局进行配置，
     * 否则当前端页面传的数组数据长度大于256位的时候就会报错
     */
    @PostMapping(URL_TENANT)
    public Result<Integer> addTenant(@RequestBody @Validated(Add.class) TenantDto tenantDto) {
        return toResult(tenantService.addTenant(addPhoneIfAbsent(tenantDto.getPhone()),
                toTenantPo(tenantDto), tenantDto.getResourceIds()));
    }

    @PutMapping(URL_TENANT)
    public Result<Integer> updateTenant(@RequestBody @Validated(Update.class) TenantDto tenantDto) {
        return toResult(tenantService.updateTenant(toTenantSelectivePo(tenantDto), tenantDto.getResourceIds()));
    }

    @PutMapping(PUT_TENANT_STATUS)
    public Result<Integer> updateStatus(
            @PathVariable("tenantId") int tenantId, @PathVariable("status") ActiveStatus status) {
        return toResult(tenantService.updateById(tenantId, TenantPo::getStatus, status));
    }

    @DeleteMapping(URL_TENANT + "/{id}")
    public Result<Integer> deleteTenant(@PathVariable("id") Integer id) {
        return toResult(tenantService.delete(id));
    }

    @Autowired
    public void setTenantService(TenantService tenantService) {
        this.tenantService = tenantService;
    }
}
