package org.isite.commons.lang.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.deepoove.poi.XWPFTemplate.compile;
import static com.deepoove.poi.config.Configure.builder;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class WordAccessor {

    private WordAccessor() {
    }

    /**
     * 生成文件
     * @param input 模板文件输入流
     * @param data 模板数据
     * @param output 文件输出流
     */
    public static void process(InputStream input, Object data, OutputStream output) throws IOException {
        compile(input, builder().useSpringEL().build()).render(data).write(output);
    }
}
