package org.isite.commons.web.email;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.time.Duration;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.isite.commons.lang.Constants.MINUTE_SECONDS;

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
	private String authUserName;
	private String authPassword;
	private Boolean ssl;
	private Integer timeout;

	public EmailClient(String smtpHost, int smtpPort, String authUserName,
					   String authPassword, String fromAddress, String fromName) {
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.authUserName = authUserName;
		this.authPassword = authPassword;
		this.fromAddress = fromAddress;
		this.fromName = fromName;
	}

	private HtmlEmail initEmail(String toAddress, String subject) throws EmailException {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(smtpHost);
		email.setCharset(UTF_8.name());
		email.addTo(toAddress);
		email.setFrom(fromAddress, fromName);
		email.setAuthentication(authUserName, authPassword);
		email.setSubject(subject);

		if (null == timeout) {
			timeout = MINUTE_SECONDS;
		}
		Duration duration = of(timeout, SECONDS);
		email.setSocketConnectionTimeout(duration);
		email.setSocketTimeout(duration);

		if (isTrue(ssl)) {
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