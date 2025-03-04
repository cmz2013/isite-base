package org.isite.commons.cloud.utils;

import org.springframework.core.env.Environment;
/**
 * @Description 读取配置。<br>Bean加载完成以后才可以使用PropertyUtils
 * @Author <font color='blue'>zhangcm</font>
 */
public class PropertyUtils {

	private PropertyUtils() {
	}

	/**
	 * 属性KEY：serviceId
	 */
	private static final String SPRING_APPLICATION_NAME = "spring.application.name";
	/**
	 * 属性KEY：端口号
	 */
	private static final String SERVER_PORT = "server.port";
	/**
	 * 读取属性
	 */
	public static String getProperty(String key) {
		return ApplicationContextUtils.getBean(Environment.class).getProperty(key);
	}

	/**
	 * 读取 serviceId
	 */
	public static String getApplicationName() {
		return getProperty(SPRING_APPLICATION_NAME);
	}

	/**
	 * 读取端口号
	 */

	public static Integer getPort() {
		return getProperty(SERVER_PORT, Integer.class);
	}

	/**
	 * 读取属性
	 */
	public static <T> T getProperty(String key, Class<T> tClass) {
		return ApplicationContextUtils.getBean(Environment.class).getProperty(key, tClass);
	}

}
