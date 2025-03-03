package org.isite.data.client;

import org.apache.commons.lang3.StringUtils;
import org.isite.commons.cloud.data.constants.HttpHeaders;
import org.isite.commons.cloud.data.enums.HttpMethod;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.enums.ChronoUnit;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.web.http.ContentType;
import org.isite.commons.web.http.HttpClient;
import org.isite.commons.web.http.HttpUtils;
import org.isite.data.support.enums.WsProtocol;
import org.isite.data.support.vo.DataApi;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 请求REST服务接口的客户端
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class RestClient extends WsClient {

    public RestClient() {
        super();
    }

	@Override
	public String[] call(DataApi api, Map<String, String> headers, Object... data) throws Exception {
		if (null == headers) {
			headers = new HashMap<>(Constants.ONE);
		}
		headers.put(HttpHeaders.CONTENT_TYPE, api.getContentType());
		long timeout = ChronoUnit.MINUTE.getMillis();
		if (null != api.getTimeout() && api.getTimeout() > Constants.ZERO) {
			timeout = api.getTimeout();
		}
		String results = HttpClient.request(HttpMethod.valueOf(api.getMethod()),
				api.getServerUrl(), headers, formatParams(api, data), timeout);
		return StringUtils.isNotBlank(results) ? new String[] { results } : null;
	}

	/**
	 * 格式化接口参数
	 * @param api 数据接口
	 * @param data 接口参数
	 * @return 请求参数
	 */
	private String formatParams(DataApi api, Object... data) {
		if (StringUtils.isNotBlank(api.getContentType()) &&
				api.getContentType().contains(ContentType.APPLICATION_JSON)) {
			return (data[Constants.ZERO] instanceof CharSequence ?
					data[Constants.ZERO].toString() : Jackson.toJsonString(data[Constants.ZERO]));
		}
		return HttpUtils.toFormData(new ParameterGenerator().generate(api, data));
	}

	@Override
	public WsProtocol[] getIdentities() {
		return new WsProtocol[] { WsProtocol.REST };
	}
}
