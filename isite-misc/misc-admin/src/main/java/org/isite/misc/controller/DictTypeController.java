package org.isite.misc.controller;

import com.github.pagehelper.Page;
import org.isite.commons.lang.data.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.misc.cache.DictCache;
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

import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.cloud.data.Converter.toPageQuery;
import static org.isite.misc.data.constants.UrlConstants.URL_MISC;

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
    @GetMapping(URL_MISC + "/dict/types")
    public PageResult<DictType> findPage(PageRequest<DictTypeDto> request) {
        try (Page<DictTypePo> page = dictTypeService.findPage(toPageQuery(request, DictTypePo::new))) {
            return toPageResult(request, convert(page.getResult(), DictType::new), page.getTotal());
        }
    }

    /**
     * 新增字典类型
     */
    @PostMapping(URL_MISC + "/dict/type")
    public Result<Integer> addDictType(@RequestBody @Validated(Add.class) DictTypeDto type) {
        isFalse(dictTypeService.exists(DictTypePo::getValue, type.getValue()),
                getMessage("DictType.exists", "the type already exists"));
        return toResult(dictTypeService.insert(convert(type, DictTypePo::new)));
    }

    /**
     * 更新字典类型
     */
    @PutMapping(URL_MISC + "/dict/type")
    public Result<Integer> updateDictType(
            @RequestBody @Validated(Update.class) DictTypeDto typeDto) {
        String dictType = dictTypeService.get(typeDto.getId()).getValue();
        DictTypePo typePo = convert(typeDto, DictTypePo::new);
        if (!dictType.equals(typeDto.getValue())) {
            isFalse(dictTypeService.exists(DictTypePo::getValue, typeDto.getValue()),
                    getMessage("DictType.exists", "the type already exists"));
            return toResult(dictCache.updateDictType(typePo, dictType));
        }
        return toResult(dictTypeService.updateById(typePo));
    }

    /**
     * 删除字典类型和数据
     */
    @DeleteMapping(URL_MISC + "/dict/type/{type}")
    public Result<Integer> deleteDictType(@PathVariable("type") String type) {
        return toResult(dictCache.deleteDictType(type));
    }
}

