package org.isite.misc.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.cache.DictCache;
import org.isite.misc.data.constants.MiscUrls;
import org.isite.misc.data.dto.DictDataDto;
import org.isite.misc.data.vo.DictData;
import org.isite.misc.service.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Description 字典数据Controller
 * 在字典类型列表，点击类型下钻到数据列表
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DictDataController extends BaseController {
    private DictCache dictCache;
    private DictDataService dictDataService;

    @Autowired
    public void setDictCache(DictCache dictCache) {
        this.dictCache = dictCache;
    }

    @Autowired
    public void setDictDataService(DictDataService dictDataService) {
        this.dictDataService = dictDataService;
    }

    /**
     * 根据字典类型查询数据.在字典类型列表，点击类型下钻到数据列表
     */
    @GetMapping(MiscUrls.URL_MISC + "/dict/data/{type}")
    public Result<List<DictData>> findList(@PathVariable("type") String type) {
        return toResult(dictCache.findByType(type));
    }

    @GetMapping(MiscUrls.URL_MISC + "/dict/data/{id}")
    public Result<DictData> findById(@PathVariable("id") Integer id) {
        return toResult(DataConverter.convert(dictDataService.get(id), DictData::new));
    }

    @PostMapping(MiscUrls.URL_MISC + "/dict/data")
    public Result<Integer> addDictData(@RequestBody @Validated(Add.class) DictDataDto dataDto) {
        return toResult(dictCache.addDictData(dataDto));
    }

    @PutMapping(MiscUrls.URL_MISC + "/dict/data")
    public Result<Integer> updateDictData(
            @RequestBody @Validated(Update.class) DictDataDto dataDto) {
        return toResult(dictCache.updateDictData(dataDto));
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping(MiscUrls.URL_MISC + "/dict/data/{dataId}")
    public Result<Integer> deleteDictData(@PathVariable("dataId") Integer dataId) {
        return toResult(dictCache.deleteDictData(dictDataService.get(dataId)));
    }
}

