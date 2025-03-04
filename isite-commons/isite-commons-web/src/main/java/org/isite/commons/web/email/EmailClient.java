package org.isite.commons.web.email;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.isite.commons.lang.Constants;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
public class EmailClient {
	private String smtpHost;
	private Integer smtpPort;
	private String fromAddress;
	private String fromName;
	private String authUsername;
	private String authPassword;
	private Boolean ssl;
	private Integer timeout;

	public EmailClient(String smtpHost, int smtpPort, String authUsername,
					   String authPassword, String fromAddress, String fromName) {
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.authUsername = authUsername;
		this.authPassword = authPassword;
		this.fromAddress = fromAddress;
		this.fromName = fromName;
	}

	private HtmlEmail initEmail(String toAddress, String subject) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(smtpHost);
		email.setCharset(StandardCharsets.UTF_8.name());
		email.addTo(toAddress);
		email.setFrom(fromAddress, fromName);
		email.setAuthentication(authUsername, authPassword);
		email.setSubject(subject);

		if (null == timeout) {
			timeout = Constants.MINUTE_SECOND;
		}
		Duration duration = Duration.of(timeout, ChronoUnit.SECONDS);
		email.setSocketConnectionTimeout(duration);
		email.setSocketTimeout(duration);
		if (BooleanUtils.isTrue(ssl)) {
			email.setSslSmtpPort(smtpPort.toString());
			email.setSSLOnConnect(ssl);
		} else {
			email.setSmtpPort(smtpPort);
		}
		return email;
	}

	/**
	 * 发送普通邮件
	 * @param to 收信人地址
	 * @param subject email主题
	 * @param message 发送email信息
	 */
	public void sendEmail(String to, String subject, String message) throws EmailException {
		HtmlEmail email = initEmail(to, subject);
		email.setMsg(message);
		email.send();
	}
}