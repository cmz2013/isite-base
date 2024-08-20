package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;
import org.isite.misc.data.enums.FileStatus;

import javax.validation.constraints.NotNull;

/**
 * @Description 文件记录。可以统一管理文件上传记录、文件异步导出记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class FileRecordDto extends Dto<Integer> {
    /**
     * 文件名称
     */
    @NotNull(groups = Add.class)
    private String fileName;
    /**
     * HTTP接口，或FTP文件存放路径
     */
    @NotNull(groups = Add.class)
    private String target;
    /**
     * 状态
     */
    @NotNull(groups = Add.class)
    private FileStatus status;
    /**
     * 备注
     */
    private String remark;
}
