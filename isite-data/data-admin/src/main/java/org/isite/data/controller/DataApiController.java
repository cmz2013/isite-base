package org.isite.data.controller;

import org.isite.commons.lang.data.Result;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.data.po.DataApiPo;
import org.isite.data.service.DataApiService;
import org.isite.data.support.dto.DataApiDto;
import org.isite.data.support.vo.DataApi;
import org.isite.jpa.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.data.support.constants.UrlConstants.URL_DATA;

/**
 * @Description 数据接口Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DataApiController extends BaseController {

    private DataApiService dataApiService;

    /**
     * 添加数据接口
     */
    @PostMapping(URL_DATA + "/api")
    public Result<Long> addDataApi(
            @RequestBody @Validated(Add.class) DataApiDto dataApiDto) {
        return toResult(dataApiService.insert(convert(dataApiDto, DataApiPo::new)));
    }

    /**
     * 修改数据接口
     */
    @PutMapping(URL_DATA + "/api")
    public Result<Long> updateDataApi(
            @RequestBody @Validated(Update.class) DataApiDto dataApiDto) {
        return toResult(dataApiService.updateById(convert(dataApiDto, DataApiPo::new)));
    }

    /**
     * 查询接口
     */
    @GetMapping(URL_DATA + "/api/{id}")
    public Result<DataApi> getDataApi(@PathVariable("id") String id) {
        return toResult(convert(dataApiService.get(id), DataApi::new));
    }

    /**
     * 启停接口
     */
    @PutMapping(URL_DATA + "/api/{id}/status/{status}")
    public Result<Long> updateDataApi(
            @PathVariable("id") String id, @PathVariable("status") SwitchStatus status) {
        return toResult(dataApiService.updateById(id, status));
    }

    /**
     * 查询接口列表
     */
    @GetMapping(URL_DATA + "/apis")
    public PageResult<DataApi> findPage(PageRequest<DataApiDto> request) {
        Page<DataApiPo> page = dataApiService.findPage(toPageQuery(request, DataApiPo::new));
        return toPageResult(request, convert(page.getResult(), DataApi::new), page.getTotal());
    }

    @Autowired
    public void setDataApiService(DataApiService dataApiService) {
        this.dataApiService = dataApiService;
    }
}
