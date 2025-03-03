package org.isite.misc.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.cloud.utils.MessageUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.cache.DictCache;
import org.isite.misc.converter.DictTypeConverter;
import org.isite.misc.data.constants.MiscUrls;
import org.isite.misc.data.dto.DictTypeDto;
import org.isite.misc.data.vo.DictType;
import org.isite.misc.po.DictTypePo;
import org.isite.misc.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 字典数据Controller
 * 在字典类型列表，点击类型下钻到数据列表
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DictTypeController extends BaseController {

    private DictCache dictCache;
    private DictTypeService dictTypeService;

    @Autowired
    public void setDictTypeService(DictTypeService dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    @Autowired
    public void setDictCache(DictCache dictCache) {
        this.dictCache = dictCache;
    }

    /**
     * 查询字典类型列表
     */
    @GetMapping(MiscUrls.URL_MISC + "/dict/types")
    public PageResult<DictType> findPage(PageRequest<DictTypeDto> request) {
        try (Page<DictTypePo> page = dictTypeService.findPage(PageQueryConverter.toPageQuery(request, DictTypePo::new))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), DictType::new), page.getTotal());
        }
    }

    /**
     * 新增字典类型
     */
    @PostMapping(MiscUrls.URL_MISC + "/dict/type")
    public Result<Integer> addDictType(@RequestBody @Validated(Add.class) DictTypeDto dictTypeDto) {
        Assert.isFalse(dictTypeService.exists(DictTypePo::getValue, dictTypeDto.getValue()),
                MessageUtils.getMessage("DictType.exists", "the type already exists"));
        return toResult(dictTypeService.insert(DictTypeConverter.toDictTypePo(dictTypeDto)));
    }

    /**
     * 更新字典类型
     */
    @PutMapping(MiscUrls.URL_MISC + "/dict/type")
    public Result<Integer> updateDictType(
            @RequestBody @Validated(Update.class) DictTypeDto typeDto) {
        String dictType = dictTypeService.get(typeDto.getId()).getValue();
        DictTypePo typePo = DataConverter.convert(typeDto, DictTypePo::new);
        if (!dictType.equals(typeDto.getValue())) {
            Assert.isFalse(dictTypeService.exists(DictTypePo::getValue, typeDto.getValue()),
                    MessageUtils.getMessage("DictType.exists", "the type already exists"));
            return toResult(dictCache.updateDictType(typePo, dictType));
        }
        return toResult(dictTypeService.updateById(typePo));
    }

    /**
     * 删除字典类型和数据
     */
    @DeleteMapping(MiscUrls.URL_MISC + "/dict/type/{type}")
    public Result<Integer> deleteDictType(@PathVariable("type") String type) {
        return toResult(dictCache.deleteDictType(type));
    }
}

