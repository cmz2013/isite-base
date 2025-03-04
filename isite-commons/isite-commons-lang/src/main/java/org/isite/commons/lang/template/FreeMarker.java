package org.isite.commons.lang.template;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.isite.commons.lang.Constants;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class FreeMarker {

    private FreeMarker() {
    }

    /**
     * 构造 Configuration
     */
    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        //freemarker默认将数字按3位使用逗号分割，设定freemarker数值格式化
        configuration.setNumberFormat(Constants.POUND_SIGN);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return configuration;
    }

    /**
     * 处理字符串模板
     * @param template 模板内容
     * @param context 模板数据
     */
    public static String process(String template, Object context) throws IOException, TemplateException {
        Configuration configuration = getConfiguration();
        configuration.setTemplateLoader(new StringTemplateLoader());
        StringWriter out = new StringWriter();
        // 将模板内容转换为string
        new Template(null, template, configuration).process(context, out);
        return out.getBuffer().toString();
    }

    /**
     * 从根目录找和处理模板文件
     * @param baseDir 模板文件根目录
     * @param name 模板文件名
     * @param context 模板数据
     */
    public static String process(File baseDir, String name, Object context) throws IOException, TemplateException {
        Configuration configuration = getConfiguration();
        configuration.setTemplateLoader(new FileTemplateLoader(baseDir));
        StringWriter out = new StringWriter();
        // 将模板内容转换为string
        configuration.getTemplate(name).process(context, out);
        return out.getBuffer().toString();
    }

    /**
     * 从类路径寻找和处理模板文件
     * @param packagePath 报路径  eg：/templates
     * @param name 模板文件名
     * @param context 模板数据
     */
    public static String process(String packagePath, String name, Object context) throws IOException, TemplateException {
        Configuration configuration = getConfiguration();
        configuration.setTemplateLoader(new ClassTemplateLoader(FreeMarker.class, packagePath));
        StringWriter out = new StringWriter();
        // 将模板内容转换为string
        configuration.getTemplate(name).process(context, out);
        return out.getBuffer().toString();
    }
}
