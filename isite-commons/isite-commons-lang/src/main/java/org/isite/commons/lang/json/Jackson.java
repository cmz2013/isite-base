package org.isite.commons.lang.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static java.lang.Boolean.FALSE;
import static java.lang.System.arraycopy;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.isite.commons.lang.Constants.ONE;
import static org.isite.commons.lang.Constants.ZERO;

/**
 * @Description JSON工具类
 * @Author <font color='blue'>zhangcm</font>
 */
public class Jackson {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	static {
		//如果是空对象的时候,不抛异常,也就是对应的属性没有get方法
		OBJECT_MAPPER.configure(FAIL_ON_EMPTY_BEANS, FALSE);
		//反序列化的时候如果多了其他属性,不抛出异常
		OBJECT_MAPPER.configure(FAIL_ON_UNKNOWN_PROPERTIES, FALSE);
		OBJECT_MAPPER.setSerializerFactory(OBJECT_MAPPER.getSerializerFactory()
				.withSerializerModifier(new DefaultSerializerModifier()));
	}

	/**
	 * 私有化构造函数，禁止构造实例
	 */
	private Jackson() {
	}

	/**
	 * bean、array、List、Map转json字符串
	 */
	@SneakyThrows
	public static String toJsonString(Object object) {
		return OBJECT_MAPPER.writeValueAsString(object);
	}

	/**
	 * json字符串转Bean
	 */
	@SneakyThrows
	public static <T> T parseObject(String content, Class<T> tClass) {
		return OBJECT_MAPPER.readValue(content, tClass);
	}

	/**
	 * json字符串转对象
	 *
	 * @param content          json字符串
	 * @param tClass           对象类
	 * @param parameterClasses 对象类泛型参数
	 */
	@SneakyThrows
	public static <T> T parseObject(String content, Class<T> tClass, Class<?>... parameterClasses) {
		JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(tClass, parameterClasses);
		return OBJECT_MAPPER.readValue(content, javaType);
	}

	/**
	 * @Description json字符串转List
	 * @param content json字符串
	 * @param tClass List泛型参数
	 * @param classes T的泛型参数
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> parseArray(String content, Class<T> tClass, Class<?>... classes) {
		if (isEmpty(classes)) {
			return parseObject(content, ArrayList.class, tClass);
		}
		Class<?>[] parameterClasses = new Class[ONE + classes.length];
		parameterClasses[ZERO] = tClass;
		arraycopy(classes, ZERO, parameterClasses, ONE, classes.length);
		return parseObject(content, ArrayList.class, parameterClasses);
	}
}
