package com.ph.ibm.util;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class EmailUtils {

	private static Properties createEmailProperties() {
		Properties emailProperties = new Properties();
		emailProperties.put("mail.smtp.host", OpumConstants.EMAIL_SMTP_MAIL_HOST);
		emailProperties.put("mail.smtp.socketFactory.port", OpumConstants.EMAIL_PORT);
		emailProperties.put("mail.smtp.starttls.enable", "true");
		emailProperties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.port", OpumConstants.EMAIL_PORT);
		return emailProperties;
	}

	public static Session createEmailSession() {
		Properties emailProps = createEmailProperties();
		Session emailSession = Session.getDefaultInstance(emailProps,
			new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(OpumConstants.EMAIL_SENDER, OpumConstants.EMAIL_PASSWORD);
				}
			});
		return emailSession;
	}
}
