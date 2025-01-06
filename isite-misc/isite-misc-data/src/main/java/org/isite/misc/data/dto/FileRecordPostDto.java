package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.misc.data.enums.FileStatus;

import javax.validation.constraints.NotNull;

/**
 * @Description 文件记录。可以统一管理文件上传记录、文件异步导出记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class FileRecordPostDto {
    /**
     * 文件名称
     */
    @NotNull
    private String fileName;
    /**
     * HTTP接口，或FTP文件存放路径
     */
    @NotNull
    private String target;
    /**
     * 状态
     */
    @NotNull
    private FileStatus status;
    /**
     * 备注
     */
    private String remark;
}
