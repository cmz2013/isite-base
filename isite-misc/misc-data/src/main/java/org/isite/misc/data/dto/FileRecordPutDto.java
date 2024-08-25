package org.isite.misc.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Update;
import org.isite.misc.data.enums.FileStatus;

import javax.validation.constraints.NotNull;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@NoArgsConstructor
public class FileRecordPutDto extends Dto<Integer> {
    /**
     * 状态
     */
    @NotNull(groups = Update.class)
    private FileStatus status;
    /**
     * 备注
     */
    private String remark;

    public FileRecordPutDto(Integer id, FileStatus status, String remark) {
        this.setId(id);
        this.status = status;
        this.remark = remark;
    }
}
