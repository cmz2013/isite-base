package org.isite.data.callback;

import lombok.SneakyThrows;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.util.function.Predicate;

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import static org.isite.commons.lang.Reflection.getGenericParameters;
import static org.isite.commons.lang.Constants.EXTENSION_FTL;
import static org.isite.commons.lang.Constants.EXTENSION_XML;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.lang.template.xml.Thymeleaf.process;

/**
 * @Description 回调接口处理过程抽象类
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class XmlCallback<P, R> extends DataCallback<P, R> {
	/**
	 * 接口参数class
	 */
	private final Class<P> dataClass;
	/**
	 * 接口返回值class
	 */
	private final Class<R> resultClass;

	protected XmlCallback(Predicate<R> predicate) {
		super(predicate);
		Class<?>[] classes = getGenericParameters(this.getClass(), XmlCallback.class);
		this.dataClass = cast(classes[ZERO]);
		this.resultClass = cast(classes[ONE]);
	}

	@Override
	@SneakyThrows
	protected String formatData(P data) {
		if (null == data) {
			return null;
		}
		return process(
				data.getClass().getResourceAsStream(data.getClass().getSimpleName() + EXTENSION_FTL),
				data);
	}

	@Override
	@SneakyThrows
	protected R toResult(String[] results) {
		return newLoader(new FromXmlRulesModule() {
			@Override
			protected void loadRules() {
				loadXMLRules(new InputSource(resultClass.getResourceAsStream(resultClass.getSimpleName() + EXTENSION_XML)));
			}
		}).newDigester().parse(new ByteArrayInputStream(results[ZERO].getBytes()));
	}

	@Override
	@SneakyThrows
	protected P toData(String[] data) {
		return newLoader(new FromXmlRulesModule() {
			@Override
			protected void loadRules() {
				loadXMLRules(new InputSource(dataClass.getResourceAsStream(dataClass.getSimpleName() + EXTENSION_XML)));
			}
		}).newDigester().parse(new ByteArrayInputStream(data[ZERO].getBytes()));
	}
}
