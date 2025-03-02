package org.isite.misc.controller;

import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.data.dto.FileRecordPutDto;
import org.isite.misc.po.FileRecordPo;
import org.isite.misc.service.FileRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.misc.converter.FileRecordConverter.toFileRecordPo;
import static org.isite.misc.data.constants.MiscUrls.MY_POST_FILE_RECORD;
import static org.isite.misc.data.constants.MiscUrls.PUT_FILE_RECORD;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class FileRecordController extends BaseController {

    private FileRecordService fileRecordService;

    @PostMapping(PUT_FILE_RECORD)
    public Result<Integer> updateFileRecord(@Validated @RequestBody FileRecordPutDto fileRecordPutDto) {
        return toResult(fileRecordService.updateSelectiveById(convert(fileRecordPutDto, FileRecordPo::new)));
    }

    @PostMapping(MY_POST_FILE_RECORD)
    public Result<Integer> addFileRecord(@Validated @RequestBody FileRecordPostDto fileRecordPostDto) {
        FileRecordPo fileRecordPo = toFileRecordPo(fileRecordPostDto);
        fileRecordService.insert(fileRecordPo);
        return toResult(fileRecordPo.getId());
    }

    @Autowired
    public void setFileRecordService(FileRecordService fileRecordService) {
        this.fileRecordService = fileRecordService;
    }
}
