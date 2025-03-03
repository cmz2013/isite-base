package org.isite.data.handler;

import lombok.SneakyThrows;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.lang.file.FileUtils;
import org.isite.commons.lang.template.xml.Thymeleaf;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
/**
 * @Description xml数据接口的处理过程
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class XmlHandler<P, R> extends DataHandler<P, R> {

	protected XmlHandler(WsFunction<P, R> wsFunction) {
		super(wsFunction);
	}

	/**
	 * 基于规则文件解析xml格式的请求数据.
	 * 注意：必须在数据类所在的目录下创建用于解析数据的规则文件：数据类名+.rule
	 */
	@Override
	@SneakyThrows
	protected P toData(String data, Class<P> pClass) {
		if (StringUtils.isBlank(data)) {
			return null;
		}
		return DigesterLoader.newLoader(new FromXmlRulesModule() {
			@Override
			protected void loadRules() {
				loadXMLRules(new InputSource(pClass.getResourceAsStream(pClass.getSimpleName() + FileUtils.EXTENSION_XML)));
			}
		}).newDigester().parse(new ByteArrayInputStream(data.getBytes()));
	}

	/**
	 * 将result转换为xml数据，即{@code AbstractService.handle()的返回值}
	 * 注意：Result类提供了通用的xml模板，如果需要自定义，必须在Result子类所在的目录下创建模板文件：Result子类名+.tpl
	 */
	@Override
	@SneakyThrows
	protected String formatResult(Result<R> result) {
		if (null == result) {
			return null;
		}
		return Thymeleaf.process(
				result.getClass().getResourceAsStream(result.getClass().getSimpleName() + FileUtils.EXTENSION_FTL),
				result);
	}
}

