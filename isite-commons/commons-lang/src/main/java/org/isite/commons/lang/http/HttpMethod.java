package org.isite.commons.lang.http;

import org.isite.commons.lang.enums.Enumerable;

/**
 * Http Method
 * @author <font color='blue'>zhangcm</font>
 */
public enum HttpMethod implements Enumerable<String> {

    POST, GET, PUT, DELETE;

    @Override
    public String getCode() {
        return this.name();
    }
}