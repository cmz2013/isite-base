package org.isite.misc.converter;

import org.isite.commons.cloud.converter.DataConverter;
import org.isite.commons.lang.Constants;
import org.isite.commons.web.interceptor.TransmittableHeaders;
import org.isite.misc.data.dto.FileRecordPostDto;
import org.isite.misc.po.FileRecordPo;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FileRecordConverter {

    private FileRecordConverter() {
    }

    public static FileRecordPo toFileRecordPo(FileRecordPostDto fileRecordPostDto) {
        FileRecordPo fileRecordPo = DataConverter.convert(fileRecordPostDto, FileRecordPo::new);
        fileRecordPo.setUserId(TransmittableHeaders.getUserId());
        if (null == fileRecordPo.getRemark()) {
            fileRecordPo.setRemark(Constants.BLANK_STR);
        }
        return fileRecordPo;
    }
}
