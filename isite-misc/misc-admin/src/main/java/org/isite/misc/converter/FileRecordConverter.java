package org.isite.misc.converter;

import org.isite.misc.data.dto.FileRecordDto;
import org.isite.misc.po.FileRecordPo;

import static org.isite.commons.cloud.data.Converter.convert;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FileRecordConverter {

    private FileRecordConverter() {
    }

    public static FileRecordPo toFileRecordPo(FileRecordDto fileRecordDto, Long userId) {
        FileRecordPo fileRecordPo = convert(fileRecordDto, FileRecordPo::new);
        fileRecordPo.setUserId(userId);
        return fileRecordPo;
    }
}
