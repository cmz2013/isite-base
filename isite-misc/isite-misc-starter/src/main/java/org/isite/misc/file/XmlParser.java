package org.isite.misc.file;

import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.file.FileUtils;
import org.xml.sax.InputSource;

import java.io.InputStream;
/**
 * @Description 解析XML文件
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class XmlParser<T> extends Parser<T> {

    @Override
    protected T toData(InputStream input) throws Exception {
        Class<?> dataClass = Reflection.getGenericParameter(this.getClass(), XmlParser.class);
        return DigesterLoader.newLoader(new FromXmlRulesModule() {
            @Override
            protected void loadRules() {loadXMLRules(
                    new InputSource(dataClass.getResourceAsStream(dataClass.getSimpleName() + FileUtils.EXTENSION_XML)));
            }
        }).newDigester().parse(input);
    }
}
