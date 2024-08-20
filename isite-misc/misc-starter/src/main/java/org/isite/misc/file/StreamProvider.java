package org.isite.misc.file;

import java.io.InputStream;

/**
 * @Description 构造文件流
 * @Author <font color='blue'>zhangcm</font>
 */
@FunctionalInterface
public interface StreamProvider {

    InputStream stream();
}
