package org.isite.data.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.dto.Dto;

/**
 * @Description 接口日志DTO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DataLogDto extends Dto<String> {
	/**
	 * 接口ID
	 */
	private String apiId;
	/**
	 * 执行器编码
	 */
	private String appCode;
	/**
	 * 接口实现类
	 */
	private String apiClass;
	/**
	 * 本地服务接口参数类(XML、JSON服务接口必填)
	 */
	private String paramClass;
	/**
	 * 接口参数
	 */
	private String reqData;
	/**
	 * 接口返回数据
	 */
	private String repData;
	/**
	 * 接口执行结果
	 */
	private Boolean status;

	private String remark;

	public DataLogDto() {
		super();
	}

	public DataLogDto(String appCode, String apiId, String apiClass, String reqData) {
		super();
		this.appCode = appCode;
		this.apiId = apiId;
		this.reqData = reqData;
		this.apiClass = apiClass;
	}

	public DataLogDto(String appCode, String apiId, String apiClass, String paramClass, String reqData) {
		this.appCode = appCode;
		this.apiId = apiId;
		this.apiClass = apiClass;
		this.paramClass = paramClass;
		this.reqData = reqData;
	}
}
