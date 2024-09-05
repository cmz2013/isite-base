package org.isite.data.support.vo;

import lombok.Getter;
import lombok.Setter;
import org.isite.commons.cloud.data.Vo;
import org.isite.commons.lang.enums.SwitchStatus;
import org.isite.data.support.enums.WsProtocol;
import org.isite.data.support.enums.WsType;

/***
 * @Description 数据接口
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class DataApi extends Vo<String> {
	/**
	 * 执行器应用程序编码
	 */
	private String appCode;
	/**
	 * 远程接口地址（执行器本地接口不配置）
	 */
	private String serverUrl;
	/**
	 * 接口类型(0：远程接口，1:本地接口)
	 */
	private WsType wsType;
	/**
	 * 接口协议
	 */
	private WsProtocol wsProtocol;
	/**
	 * 方法名称
	 */
	private String method;
	/**
	 * 接口参数名称，逗号分隔。非必填项
	 */
	private String args;
	/**
	 * HTTP Content-Type。非必填项
	 */
	private String contentType;
	/**
	 * SOAP Header/Body元素命名空间(xmlns属性)
	 */
	private String wsNameSpace;
	/**
	 * SOAP Header/Body元素前缀
	 */
	private String wsPointName;
	/**
	 * 请求超时(毫秒)。非必填项
	 */
	private Integer timeout;
	/**
	 * 启用/停用
	 */
	private SwitchStatus status;
	/**
	 * 用于接收告警信息的电子邮箱，多个邮箱因为逗号号隔开
	 */
	private String emails;
}
