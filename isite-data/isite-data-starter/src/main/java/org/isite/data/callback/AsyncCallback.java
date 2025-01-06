package org.isite.data.callback;

/**
 * @Description 异步调用时的回调函数
 * @Author <font color='blue'>zhangcm</font>
 */
public abstract class AsyncCallback {
	/**
	 * 调用成功时的回调函数
	 */
	public abstract void success(String[] results);
	/**
	 * 调用失败时的回调函数
	 */
	public abstract void error(Exception e);
}