package org.isite.commons.cloud.data.vo;

import lombok.Getter;
import lombok.Setter;

import static org.isite.commons.lang.enums.ResultStatus.OK;

/**
 * @Description 定义接口返回数据的统一格式
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class Result<T> {
	/**
	 * 状态码
	 */
	private int code;
	/**
	 * 消息
	 */
	private String message;
	/**
	 * 响应数据
	 */
	private T data;

    public static Result<Object> success() {
		return new Result<>(OK.getCode(), null);
    }

	public static <D> Result<D> success(D data) {
		return new Result<>(OK.getCode(), data);
	}

	public Result() {
	}

	public Result(int code, T data) {
		this.code = code;
		this.data = data;
	}

	public Result(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
