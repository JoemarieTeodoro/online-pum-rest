package com.ph.ibm.util;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class EmailUtils {

	private static Properties createEmailProperties() {
		Properties emailProperties = new Properties();
		emailProperties.put("mail.smtp.host", "smtp.gmail.com");
		emailProperties.put("mail.smtp.socketFactory.port", "465");
		emailProperties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.port", "465");
		return emailProperties;
	}

	public static Session createEmailSession() {
		Properties emailProps = createEmailProperties();
		Session emailSession = Session.getDefaultInstance(emailProps,
			new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("onlinepumsender@gmail.com","onlinepum");
				}
			});
		return emailSession;
	}
}
