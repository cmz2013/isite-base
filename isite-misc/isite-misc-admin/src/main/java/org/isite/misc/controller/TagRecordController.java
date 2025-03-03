package org.isite.misc.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.converter.PageQueryConverter;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.op.Add;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.data.constants.MiscUrls;
import org.isite.misc.data.dto.TagRecordDto;
import org.isite.misc.data.vo.TagRecord;
import org.isite.misc.po.TagRecordPo;
import org.isite.misc.service.TagRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description 标签记录Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class TagRecordController extends BaseController {

    private TagRecordService tagRecordService;

    /**
     * 保存标签记录
     */
    @PostMapping(MiscUrls.POST_TAG_RECORD)
    public Result<Integer> addTagRecord(@RequestBody @Validated(Add.class) TagRecordDto recordDto) {
        return toResult(tagRecordService.insert(DataConverter.convert(recordDto, TagRecordPo::new)));
    }

    /**
     * 查询标签记录
     */
    @GetMapping(MiscUrls.URL_MISC + "/tag/records")
    public PageResult<TagRecord> findPage(PageRequest<TagRecordDto> request) {
        try (Page<TagRecordPo> page = tagRecordService.findPage(PageQueryConverter.toPageQuery(request, TagRecordPo::new))) {
            return toPageResult(request, DataConverter.convert(page.getResult(), TagRecord::new), page.getTotal());
        }
    }

    @Autowired
    public void setTagRecordService(TagRecordService tagRecordService) {
        this.tagRecordService = tagRecordService;
    }
}

