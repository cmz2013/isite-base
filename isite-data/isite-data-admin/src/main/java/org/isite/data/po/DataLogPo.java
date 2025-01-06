package org.isite.data.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mongo.data.Po;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Description 数据接口日志PO
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
@Document(collection = "data_log")
public class DataLogPo extends Po<String> {
	/**
	 * 接口ID
	 */
	private String apiId;
	/**
	 * 执行器应用程序编码
	 */
	private String appCode;
	/**
	 * 接口实现类
	 *
	 * 如果使用对象序列化，一旦接口实现类发生变更，则无法兼容历史数据
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
	 * 接口响应数据
	 */
	private String repData;
	/**
	 * 接口执行结果
	 */
	private Boolean status;
	/**
	 * 备注
	 */
	private String remark;
}
