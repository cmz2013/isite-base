package org.isite.commons.cloud.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description Http Method
 * @Author <font color='blue'>zhangcm</font>
 */
public enum HttpMethod implements Enumerable<String> {

    POST, GET, PUT, DELETE;

    @Override
    public String getCode() {
        return this.name();
    }
}