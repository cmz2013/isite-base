package org.isite.commons.lang.file;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        XWPFTemplate.compile(input, Configure.builder().useSpringEL().build()).render(data).write(output);
    }
}
