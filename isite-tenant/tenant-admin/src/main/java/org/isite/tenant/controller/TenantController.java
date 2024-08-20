package org.isite.tenant.controller;

import com.github.pagehelper.Page;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.tenant.data.dto.TenantDto;
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

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.tenant.data.constant.UrlConstants.URL_TENANT;

/**
 * @Description 租户信息 Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TenantController extends BaseController {

    private TenantService tenantService;

    @GetMapping(URL_TENANT + "list")
    public PageResult<Tenant> findPage(PageRequest<TenantDto> request) {
        try (Page<TenantPo> page = tenantService.findPage(toPageQuery(request, TenantPo::new))) {
            return toPageResult(request, convert(page.getResult(), Tenant::new), page.getTotal());
        }
    }

    @GetMapping(URL_TENANT + "/{id}")
    public Result<Tenant> findById(@PathVariable("id") Integer id) {
        return toResult(convert(tenantService.get(id), Tenant::new));
    }

    /**
     * 添加租户
     */
    @PostMapping(URL_TENANT)
    public Result<Integer> addTenant(@RequestBody @Validated(Add.class) TenantDto tenant) {
        return toResult(tenantService.insert(convert(tenant, TenantPo::new)));
    }

    @PutMapping(URL_TENANT)
    public Result<Integer> updateTenant(
            @RequestBody @Validated(Update.class) TenantDto tenantDto) {
        return toResult(tenantService.updateById(convert(tenantDto, TenantPo::new)));
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
