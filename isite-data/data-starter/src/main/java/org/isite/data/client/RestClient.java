package org.isite.data.client;

import org.isite.commons.lang.http.HttpClient;
import org.isite.data.support.enums.WsProtocol;
import org.isite.data.support.vo.DataApi;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.http.ContentType.APPLICATION_JSON;
import static org.isite.commons.lang.http.HttpHeaders.CONTENT_TYPE;
import static org.isite.commons.lang.http.HttpMethod.valueOf;
import static org.isite.commons.lang.http.HttpUtils.toFormData;
import static org.isite.commons.lang.json.Jackson.toJsonString;
import static org.isite.data.support.enums.WsProtocol.REST;

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
		HttpClient httpClient = new HttpClient();
		if (null != api.getTimeout() && api.getTimeout() > ZERO) {
			httpClient.setTimeout(api.getTimeout());
		}
		if (null == headers) {
			headers = new HashMap<>(ONE);
		}
		headers.put(CONTENT_TYPE, api.getContentType());
		String results = httpClient.request(
				valueOf(api.getMethod()), api.getServerUrl(), headers, formatParams(api, data));
		return null != results ? new String[]{results} : null;
	}

	/**
	 * 格式化接口参数
	 * @param api 数据接口
	 * @param data 接口参数
	 * @return 请求参数
	 */
	private String formatParams(DataApi api, Object... data) {
		if (isNotBlank(api.getContentType()) && api.getContentType().contains(APPLICATION_JSON)) {
			return (data[ZERO] instanceof CharSequence ? data[ZERO].toString() : toJsonString(data[ZERO]));
		}
		return toFormData(new ParameterGenerator().generate(api, data));
	}

	@Override
	public WsProtocol[] getIdentities() {
		return new WsProtocol[] {REST};
	}
}
