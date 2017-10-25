package com.ph.ibm.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Email;

public class EmailBO {

	private Logger logger = Logger.getLogger(EmailBO.class);

	public Response formatAndSendEmailPasswordReset(Email email) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("onlinepumsender@gmail.com","onlinepum");
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email.getSenderAddress()));
			message.setRecipients(Message.RecipientType.TO,
					convertAddressListToInternetAddresses(email.getRecipientAddresses()));
			message.setSubject(email.getSubject());
			message.setText(email.getText());
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return Response.status(Status.OK)
				.header("Location", "" + "employee/")
				.entity("email sent successfully")
				.build();
	}
	
	private InternetAddress[] convertAddressListToInternetAddresses(List<String> addresses) {
		List<InternetAddress> internetAddresses = new ArrayList<>();
		for (String address : addresses) {
			try {
				internetAddresses.add(new InternetAddress(address));				
			} catch (AddressException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return internetAddresses.toArray(new InternetAddress[internetAddresses.size()]);
	}
	
}
