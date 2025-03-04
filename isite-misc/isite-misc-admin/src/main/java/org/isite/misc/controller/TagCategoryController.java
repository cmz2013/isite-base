package org.isite.misc.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.op.Update;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.converter.TagCategoryConverter;
import org.isite.misc.data.constants.MiscUrls;
import org.isite.misc.data.dto.TagCategoryDto;
import org.isite.misc.data.vo.TagCategory;
import org.isite.misc.po.TagCategoryPo;
import org.isite.misc.service.TagCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @Description 标签Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TagCategoryController extends BaseController {
    private TagCategoryService tagCategoryService;

    /**
     * 查询标签分类
     */
    @GetMapping(MiscUrls.URL_MISC + "/tag/categories")
    public Result<List<TagCategory>> findTagCategories(TagCategoryDto tagCategoryDto) {
        List<TagCategoryPo> tagCategoryPos = tagCategoryService.findList(DataConverter.convert(tagCategoryDto, TagCategoryPo::new));
        return toResult(DataConverter.convert(tagCategoryPos, TagCategory::new));
    }

    /**
     * 新增标签分类
     */
    @PostMapping(MiscUrls.URL_MISC + "/tag/category")
    public Result<Integer> addTagCategory(
            @Validated(Add.class) @RequestBody TagCategoryDto tagCategoryDto) {
        return toResult(tagCategoryService.insert(TagCategoryConverter.toTagCategoryPo(tagCategoryDto)));
    }

    /**
     * 新增标签分类
     */
    @PutMapping(MiscUrls.URL_MISC + "/tag/category")
    public Result<Integer> updateTagCategory(
            @Validated(Update.class) @RequestBody TagCategoryDto tagCategoryDto) {
        return toResult(tagCategoryService.insert(TagCategoryConverter.toTagCategoryPo(tagCategoryDto)));
    }

    @Autowired
    public void setTagCategoryService(TagCategoryService tagCategoryService) {
        this.tagCategoryService = tagCategoryService;
    }
}

