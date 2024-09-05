package org.isite.data.callback;

import java.util.function.Predicate;

import static org.isite.commons.lang.Reflection.getGenericParameters;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.commons.lang.json.Jackson.parseObject;
import static org.isite.commons.lang.json.Jackson.toJsonString;

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
		Class<?>[] classes = getGenericParameters(this.getClass(), JsonCallback.class);
		this.dataClass = cast(classes[ZERO]);
		this.resultClass = cast(classes[ONE]);
	}

	@Override
	protected R toResult(String[] results) {
		return parseObject(results[ZERO], resultClass);
	}

	@Override
	protected P toData(String[] data) {
		return parseObject(data[ZERO], dataClass);
	}

	@Override
	protected String formatData(P data) {
		return toJsonString(data);
	}
}
