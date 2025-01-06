package org.isite.security.data.enums;

import lombok.Getter;
import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 客户端ID
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public enum ClientIdentifier implements Enumerable<String> {

    DATA_ADMIN("data.admin", "数据集成管理后台");

    private final String code;
    private final String label;

    ClientIdentifier(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
