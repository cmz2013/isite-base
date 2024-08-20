package org.isite.misc.data.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.lang.data.Vo;
import org.isite.misc.data.enums.FileStatus;

/**
 * @Description 文件记录。可以统一管理文件上传记录、文件异步导出记录
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class FileRecord extends Vo<Integer> {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * HTTP接口，或FTP文件存放路径
     */
    private String target;
    /**
     * 状态
     */
    private FileStatus status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 操作人
     */
    private Long userId;
}
