package org.isite.data.support.enums;

import org.isite.commons.lang.enums.Enumerable;

/**
 * @Description 接口访问协议
 * @Author <font color='blue'>zhangcm</font>
 */
public enum WsProtocol implements Enumerable<String> {
	/**
	 * soap协议
	 */
	SOAP,
	/**
	 * rest协议
	 */
	REST;

	@Override
	public String getCode() {
		return this.name();
	}
}