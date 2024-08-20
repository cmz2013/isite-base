package org.isite.data.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Dto;
import org.isite.commons.cloud.data.op.Add;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	@NotBlank(groups = {Add.class})
	private String apiId;
	/**
	 * 执行器编码
	 */
	@NotBlank(groups = {Add.class})
	private String appCode;

	/**
	 * 接口实现类
	 */
	@NotBlank(groups = {Add.class})
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
	@NotNull(groups = {Add.class})
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
