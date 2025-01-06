package org.isite.commons.lang;

import java.io.Serializable;

/**
 * @see Reflection#toFieldName(Functions) 
 * @Author <font color='blue'>zhangcm</font>
 */
@FunctionalInterface
public interface Functions<T, R> extends Serializable {

    R apply(T t);
}
