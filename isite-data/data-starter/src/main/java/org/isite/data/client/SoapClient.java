package org.isite.data.client;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.isite.data.support.enums.WsProtocol;
import org.isite.data.support.vo.DataApi;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.axiom.om.OMAbstractFactory.getOMFactory;
import static org.apache.axiom.om.OMAbstractFactory.getSOAP11Factory;
import static org.apache.axis2.Constants.Configuration.DISABLE_SOAP_ACTION;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.isite.commons.lang.data.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
import static org.isite.data.support.enums.WsProtocol.SOAP;

/**
 * @Description 请求SOAP服务的客户端
 * @Author <font color='blue'>zhangcm</font>
 */
@Component
public class SoapClient extends WsClient {

	/**
	 * 调用WebService服务接口。
	 * @param api 接口
	 * @param data 报文数据
     */
	@Override
	public String[] call(DataApi api, Map<String, String> headers, Object... data) throws AxisFault {
		ServiceClient serviceClient = new ServiceClient();
		try {
			Options opts = new Options();
			opts.setProperty(DISABLE_SOAP_ACTION, true);
			opts.setTo(new EndpointReference(api.getServerUrl()));

			long timeOut = 10000;
			if (null != api.getTimeout() && api.getTimeout() > ZERO) {
				timeOut = api.getTimeout();
			}
			opts.setTimeOutInMilliSeconds(timeOut);
			serviceClient.setOptions(opts);
            for (SOAPHeaderBlock header : createSoapHeader(api, headers)) {
                serviceClient.addHeader(header);
            }

            Map<String, Object> params = new ParameterGenerator().generate(api, data);
			Iterator<OMElement> iterator = serviceClient.sendReceive(createSoapBody(api, params)).getChildElements();
			List<String> results = new ArrayList<>();
			while (null != iterator && iterator.hasNext()) {
				OMElement element = iterator.next();
				results.add(element.getText());
			}
			return cast(results.toArray());
		} finally {
			serviceClient.cleanupTransport();
		}
	}

	/**
	 * 创建SOAP Header信息
	 */
	private List<SOAPHeaderBlock> createSoapHeader(DataApi api, Map<String, String> headers) {
		List<SOAPHeaderBlock> results = new ArrayList<>();
		if (isNotEmpty(headers)) {
			// 利用工厂，创建命名空间
			OMNamespace namespace = null;
			if (isNotBlank(api.getWsNameSpace())) {
				namespace = getOMFactory().createOMNamespace(api.getWsNameSpace(), api.getWsPointName());
			}
			SOAPFactory soapFactory = getSOAP11Factory();
			for (Map.Entry<String, String> entry: headers.entrySet()) {
				// 利用工厂，创建消息头
				SOAPHeaderBlock soapHeader = soapFactory.createSOAPHeaderBlock(entry.getKey(), namespace);
				createHeaderChild(soapHeader, entry.getValue(), namespace);
				results.add(soapHeader);
			}
		}
		return results;
	}

	/**
	 * 创建SOAP Header信息
	 */
	private void createHeaderChild(SOAPHeaderBlock soapHeader, Object data, OMNamespace namespace) {
		if (null != data) {
			SOAPFactory soapFactory = getSOAP11Factory();
			if (data instanceof Map<?, ?>) {
				for (Map.Entry<?, ?> entry : ((Map<?, ?>) data).entrySet()) {
					SOAPHeaderBlock headerChild = soapFactory.createSOAPHeaderBlock(entry.getKey().toString(), namespace);
					createHeaderChild(headerChild, entry.getValue(), namespace);
					soapHeader.addChild(headerChild);
				}
			} else {
				soapHeader.addChild(soapFactory.createOMText(data.toString()));
			}
		}
	}

	/**
	 * 创建SOAP Body数据
	 */
	private OMElement createSoapBody(DataApi api, Map<String, Object> data) {
		OMFactory omFactory = getOMFactory();
		OMNamespace namespace = null;
		if (isNotBlank(api.getWsNameSpace())) {
			namespace = omFactory.createOMNamespace(
					api.getWsNameSpace(), api.getWsPointName());
		}

		OMElement method = omFactory.createOMElement(api.getMethod(), namespace);
		if (null != data && !data.isEmpty()) {
			for (Map.Entry<String, Object> entry : data.entrySet()) {
				OMElement value = omFactory.createOMElement(entry.getKey(), namespace);
				value.setText(null == entry.getValue() ? null : entry.getValue().toString());
				method.addChild(value);
			}
		}
		return method;
	}

	@Override
	public WsProtocol[] getIdentities() {
		return new WsProtocol[] {SOAP};
	}
}
