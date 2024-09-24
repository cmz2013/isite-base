package org.isite.data.callback;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.isite.commons.web.sign.SignSecret;
import org.isite.data.client.WsClient;
import org.isite.data.client.WsClientFactory;
import org.isite.data.exception.CallbackException;
import org.isite.data.log.LogHandler;
import org.isite.data.support.dto.DataLogDto;
import org.isite.data.support.vo.DataApi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_APP_CODE;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_SIGNATURE;
import static org.isite.commons.cloud.data.constants.HttpHeaders.X_TIMESTAMP;
import static org.isite.commons.web.sign.SignUtils.getSignature;
import static org.isite.commons.web.sign.SignUtils.getSignatureParameter;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Constants.NEW_LINE;
import static org.isite.commons.lang.Constants.THREE;
import static org.isite.commons.lang.enums.ChronoUnit.SECOND;
import static org.isite.commons.lang.utils.StringUtils.join;
import static org.isite.data.client.DataApiAccessor.callApi;
import static org.isite.data.support.constants.DataConstants.SERVICE_ID;
import static org.isite.data.support.enums.WsType.CALLBACK;

/**
 * @Description 封装数据回调接口处理流程
 * @param <P> 参数类
 * @param <R> 结果类
 * @Author <font color='blue'>zhangcm</font>
 */
@Slf4j
public abstract class DataCallback<P, R> implements InitializingBean {
	/**
	 * 用于处理接口返回数据
	 */
	private final Predicate<R> predicate;
	private WsClientFactory wsClientFactory;
	private LogHandler logHandler;
	private SignSecret signSecret;

	protected DataCallback(Predicate<R> predicate) {
		this.predicate = predicate;
	}

	/**
	 * 同步执行
	 * @param dataApi 接口
	 * @param data 接口参数
	 */
	public void execute(DataApi dataApi, P data) throws CallbackException {
		DataLogDto logDto = new DataLogDto(
				dataApi.getAppCode(), dataApi.getId(), this.getClass().getName(), formatData(data));
		try {
			WsClient wsClient = wsClientFactory.get(dataApi.getWsProtocol());
			parseResult(logDto, wsClient.call(dataApi, headers(dataApi, data), logDto.getReqData()));
			logHandler.handle(logDto);
		} catch (Exception e) {
			logHandler.handle(logDto, e);
			throw new CallbackException(e);
		}
	}

	/**
	 * 将POJO转为xml、json等格式的字符串
	 */
	protected abstract String formatData(P data);

	/**
	 * 根据日志数据执行回调接口
	 */
	public DataLogDto execute(DataLogDto logDto) {
		DataApi dataApi = getDataApi(logDto.getApiId());
		try {
			WsClient wsClient = wsClientFactory.get(dataApi.getWsProtocol());
			String[] data = logDto.getReqData().split(NEW_LINE);
			parseResult(logDto, wsClient.call(dataApi, headers(dataApi, toData(data)), data));
			logHandler.handle(logDto);
		} catch (Exception e) {
			logHandler.handle(logDto, e);
		}
		return logDto;
	}

	/**
	 * @Description 转换接口响应数据为R
	 * @param results 接口响应数据
	 */
	protected abstract R toResult(String[] results);

	/**
	 * @Description 转换接口参数为P
	 * @param data 接口响应数据
	 */
	protected abstract P toData(String[] data);

	private void parseResult(DataLogDto logDto, String[] results) {
		R data = null;
		if (isNotEmpty(results)) {
			logDto.setRepData(join(NEW_LINE, results));
			data = toResult(results);
		}
		logDto.setStatus(predicate.test(data));
	}

	/**
	 * @Description 在报文头添加接口签名信息，自定义头数据需要重写该方法。
	 * 配置远程（外部系统）接口的签名秘钥：security.signature.secret.{password/apiKey}.data-api-${id}
	 * SOAP报文可以通过类似Map＜String, Map＜String, Object＞＞的数据结构，实现节点的层次结构。
	 */
	@SneakyThrows
	protected Map<String, String> headers(DataApi dataApi, P data) {
		String apiName = new URI(dataApi.getServerUrl()).getPath();
		Map<String, Object> parameters = getSignatureParameter(data);
		long timestamp = currentTimeMillis() / SECOND.getMillis();
		String password = signSecret.password("data-api-" + dataApi.getId());
		Map<String, String> headers = new HashMap<>(THREE);
		headers.put(X_APP_CODE, dataApi.getAppCode());
		headers.put(X_TIMESTAMP, valueOf(timestamp));
		headers.put(X_SIGNATURE, getSignature(apiName, parameters, password, timestamp));
		return headers;
	}

	/**
	 * @Description 根据id查询数据接口
	 */
	protected DataApi getDataApi(String apiId) {
		return callApi(CALLBACK, apiId, signSecret.password(SERVICE_ID));
	}

	@Autowired
	public void setWsClientFactory(WsClientFactory wsClientFactory) {
		this.wsClientFactory = wsClientFactory;
	}

	@Autowired
	public void setLogHandler(LogHandler logHandler) {
		this.logHandler = logHandler;
	}

	@Autowired
	public void setSignSecret(SignSecret signSecret) {
		this.signSecret = signSecret;
	}

	@Override
	public void afterPropertiesSet() {
		notNull(this.signSecret, "signSecret must be set");
		notNull(this.logHandler, "logHandler must be set");
		notNull(this.wsClientFactory, "wsClientFactory must be set");
	}
}
