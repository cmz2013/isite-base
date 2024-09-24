package org.isite.data.handler;

import org.isite.commons.cloud.data.vo.Result;

import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.commons.lang.json.Jackson.toJsonString;

/**
 * @Description JSON数据接口处理过程
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class JsonHandler<P, R> extends DataHandler<P, R> {

	protected JsonHandler(WsFunction<P, R> wsFunction) {
		super(wsFunction);
	}

	/**
	 * result转JSON数据，即{@code AbstractService.handle()的返回值}
	 */
	@Override
	protected String formatResult(Result<R> result) {
		return toJsonString(result);
	}

	@Override
	protected P toData(String data, Class<P> pClass) {
		return null == data ? null : parseObject(data, pClass);
	}

	@Override
	protected String formatData(P data) {
		return toJsonString(data);
	}
}