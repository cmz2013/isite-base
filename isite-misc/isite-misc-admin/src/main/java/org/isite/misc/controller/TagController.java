package org.isite.misc.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.data.dto.TagDto;
import org.isite.misc.data.vo.Tag;
import org.isite.misc.po.TagPo;
import org.isite.misc.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.misc.data.constants.UrlConstants.URL_MISC;

/**
 * @Description 标签Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TagController extends BaseController {

    private TagService tagService;

    /**
     * 查询标签
     */
    @GetMapping(URL_MISC + "/tags")
    public PageResult<Tag> findPage(PageRequest<TagDto> request) {
        try (Page<TagPo> page = tagService.findPage(toPageQuery(request, TagPo::new))) {
            return toPageResult(request, convert(page.getResult(), Tag::new), page.getTotal());
        }
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

}

