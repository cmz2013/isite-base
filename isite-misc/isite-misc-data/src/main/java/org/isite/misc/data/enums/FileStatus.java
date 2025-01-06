package org.isite.misc.data.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 远程文件状态枚举
 * @Author <font color='blue'>zhangcm</font>
 */
public enum FileStatus implements Enumerable<Integer> {
    /**
     * 上传成功
     */
    UPLOAD_SUCCESS(0),
    /**
     * 解析中
     */
    PARSE_PROCESS(1),
    /**
     * 解析成功
     */
    PARSE_SUCCESS(2),
    /**
     * 解析失败
     */
    PARSE_FAILURE(3),

    /**
     * 文件待生成
     */
    EXPORT_WAITING(10),
    /**
     * 文件生成中
     */
    EXPORT_PROCESS(11),
    /**
     * 文件已生成
     */
    EXPORT_SUCCESS(12),
    /**
     * 文件导出失败
     */
    EXPORT_FAILURE(13);

    private final int code;

    FileStatus(int code) {
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
