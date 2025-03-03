package org.isite.data.callback;

import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.json.Jackson;
import org.isite.commons.lang.utils.TypeUtils;

import java.util.function.Predicate;
/**
 * @Description 回调接口处理过程抽象类
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class JsonCallback<P, R> extends DataCallback<P, R> {
	/**
	 * 接口参数class
	 */
	private final Class<P> dataClass;
	/**
	 * 接口返回值class
	 */
	private final Class<R> resultClass;

	protected JsonCallback(Predicate<R> predicate) {
		super(predicate);
		Class<?>[] classes = Reflection.getGenericParameters(this.getClass(), JsonCallback.class);
		this.dataClass = TypeUtils.cast(classes[Constants.ZERO]);
		this.resultClass = TypeUtils.cast(classes[Constants.ONE]);
	}

	@Override
	protected R toResult(String[] results) {
		return Jackson.parseObject(results[Constants.ZERO], resultClass);
	}

	@Override
	protected P toData(String[] data) {
		return Jackson.parseObject(data[Constants.ZERO], dataClass);
	}

	@Override
	protected String formatData(P data) {
		return Jackson.toJsonString(data);
	}
}
