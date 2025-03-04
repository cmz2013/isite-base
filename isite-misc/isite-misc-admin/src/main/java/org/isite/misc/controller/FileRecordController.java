package org.isite.misc.controller;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.converter.FileRecordConverter;
import org.isite.misc.data.constants.MiscUrls;
import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.data.dto.FileRecordPutDto;
import org.isite.misc.po.FileRecordPo;
import org.isite.misc.service.FileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class FileRecordController extends BaseController {
    private FileRecordService fileRecordService;

    @PostMapping(MiscUrls.PUT_FILE_RECORD)
    public Result<Integer> updateFileRecord(@Validated @RequestBody FileRecordPutDto fileRecordPutDto) {
        return toResult(fileRecordService.updateSelectiveById(DataConverter.convert(fileRecordPutDto, FileRecordPo::new)));
    }

    @PostMapping(MiscUrls.MY_POST_FILE_RECORD)
    public Result<Integer> addFileRecord(@Validated @RequestBody FileRecordPostDto fileRecordPostDto) {
        FileRecordPo fileRecordPo = FileRecordConverter.toFileRecordPo(fileRecordPostDto);
        fileRecordService.insert(fileRecordPo);
        return toResult(fileRecordPo.getId());
    }

    @Autowired
    public void setFileRecordService(FileRecordService fileRecordService) {
        this.fileRecordService = fileRecordService;
    }
}
