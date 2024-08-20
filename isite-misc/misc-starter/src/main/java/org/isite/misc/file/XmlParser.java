package org.isite.misc.file;

import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.xml.sax.InputSource;

import java.io.InputStream;

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.data.Constants.EXTENSION_XML;

/**
 * @Description 解析XML文件
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class XmlParser<T> extends Parser<T> {

    @Override
    protected T toData(InputStream input) throws Exception {
        Class<?> dataClass = getGenericParameter(this.getClass(), XmlParser.class);
        return newLoader(new FromXmlRulesModule() {
            @Override
            protected void loadRules() {loadXMLRules(
                    new InputSource(dataClass.getResourceAsStream(dataClass.getSimpleName() + EXTENSION_XML)));
            }
        }).newDigester().parse(input);
    }
}
