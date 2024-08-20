package org.isite.data.client;

import org.isite.commons.cloud.factory.Strategy;
import org.isite.data.callback.AsyncCallback;
import org.isite.data.support.enums.WsProtocol;
import org.isite.data.support.vo.DataApi;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class WsClient implements Strategy<WsProtocol> {

	private Executor executor;

	/**
	 * 同步调用服务接口
	 * @param api 接口
	 * @param headers 设置头数据。SOAP报文可以通过类似Map＜String, Map＜String, Object＞＞的数据结构，实现节点的层次结构。
	 * @param data 报文数据
	 */
	public abstract String[] call(DataApi api, Map<String, String> headers, Object... data) throws Exception;

	/**
	 * 异步调用服务接口
	 * @param api 接口
	 * @param data 报文数据
	 * @param callback 异步回调函数
	 */
	public void call(final DataApi api, Map<String, String> headers, final Object[] data, final AsyncCallback callback) {
		this.executor.execute(() -> {
			try {
				String[] results = call(api, headers, data);
				if (null != callback) {
				    callback.success(results);
                }
			} catch (Exception e) {
				if (null != callback) {
				    callback.error(e);
                }
			}
		});
	}

	@Autowired
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

}
