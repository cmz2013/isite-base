package org.isite.data.callback;

import lombok.SneakyThrows;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.file.FileUtils;
import org.isite.commons.lang.template.xml.Thymeleaf;
import org.isite.commons.lang.utils.TypeUtils;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.util.function.Predicate;
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
		Class<?>[] classes = Reflection.getGenericParameters(this.getClass(), XmlCallback.class);
		this.dataClass = TypeUtils.cast(classes[Constants.ZERO]);
		this.resultClass = TypeUtils.cast(classes[Constants.ONE]);
	}

	@Override
	@SneakyThrows
	protected String formatData(P data) {
		if (null == data) {
			return null;
		}
		return Thymeleaf.process(
				data.getClass().getResourceAsStream(data.getClass().getSimpleName() + FileUtils.EXTENSION_FTL),
				data);
	}

	@Override
	@SneakyThrows
	protected R toResult(String[] results) {
		return DigesterLoader.newLoader(new FromXmlRulesModule() {
			@Override
			protected void loadRules() {
				loadXMLRules(new InputSource(resultClass.getResourceAsStream(
						resultClass.getSimpleName() + FileUtils.EXTENSION_XML)));
			}
		}).newDigester().parse(new ByteArrayInputStream(results[Constants.ZERO].getBytes()));
	}

	@Override
	@SneakyThrows
	protected P toData(String[] data) {
		return DigesterLoader.newLoader(new FromXmlRulesModule() {
			@Override
			protected void loadRules() {
				loadXMLRules(new InputSource(dataClass.getResourceAsStream(
						dataClass.getSimpleName() + FileUtils.EXTENSION_XML)));
			}
		}).newDigester().parse(new ByteArrayInputStream(data[Constants.ZERO].getBytes()));
	}
}
