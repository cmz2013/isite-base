package org.isite.commons.lang.template.xml;

import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.utils.IoUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class Thymeleaf {

    private Thymeleaf() {
    }

    /**
     * 将数据对象data转换成xml字符串
     * @param template 模板字符串
     * @param data 模板数据
     * @return xml字符串
     */
    public static String process(String template, Object data) {
        //将Object属性值转为Map
        List<Field> fields = Reflection.getFields(data.getClass());
        Map<String, Object> variables = new HashMap<>();
        fields.forEach(field -> {
            Object value = Reflection.getValue(data, field.getName());
            if (null != value) {
                variables.put(field.getName(), value);
            }
        });
        return new TemplateEngine().process(template, new Context(null, variables));
    }

    /**
     * 将数据对象data转换成xml字符串
     * @param template 模板输入流
     * @param data 模板数据
     * @return xml字符串
     */
    public static String process(InputStream template, Object data) throws IOException {
        return process(IoUtils.getString(template), data);
    }

    /**
     * 将数据对象data转换成xml字符串
     * @param template 模板文件
     * @param data 模板数据
     * @return xml字符串
     */
    public static String process(File template, Object data) throws IOException {
        return process(new FileInputStream(template), data);
    }
}
