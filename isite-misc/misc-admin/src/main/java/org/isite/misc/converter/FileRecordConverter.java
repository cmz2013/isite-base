package org.isite.misc.converter;

import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.po.FileRecordPo;

import static org.isite.commons.cloud.data.Converter.convert;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.web.interceptor.TransmittableHeaders.getUserId;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FileRecordConverter {

    private FileRecordConverter() {
    }

    public static FileRecordPo toFileRecordPo(FileRecordPostDto fileRecordPostDto) {
        FileRecordPo fileRecordPo = convert(fileRecordPostDto, FileRecordPo::new);
        fileRecordPo.setUserId(getUserId());
        if (null == fileRecordPo.getRemark()) {
            fileRecordPo.setRemark(BLANK_STRING);
        }
        return fileRecordPo;
    }
}
