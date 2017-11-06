package com.ph.ibm.bo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import com.ph.ibm.model.ResetPassword;
import com.ph.ibm.opum.exception.OpumException;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class EmailBO {

	private Logger logger = Logger.getLogger(EmailBO.class);

	/**
	 * EmployeeRepository is a Data Access Object which contain methods to add,
	 * register, login, view, validate field/s stored in employee table - opum
	 * database
	 */
	private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

	public Response emailResetPasswordLink(Email email) throws IOException {
		
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
			String emailResetPasswordLink = generateEmailResetPasswordLink(email.getRecipientAddresses().get(0));		

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email.getSenderAddress()));
			message.setRecipients(Message.RecipientType.TO,
					convertAddressListToInternetAddresses(email.getRecipientAddresses()));
			message.setSubject(email.getSubject());
			message.setText(String.format(email.getText(), emailResetPasswordLink));
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return Response.status(Status.OK)
				.header("Location", "" + "employee/")
				.entity("email sent successfully")
				.build();
	}
	
	public boolean validateToken(ResetPassword resetPassword) throws SQLException, OpumException {
		String salt = employeeRepository.retrieveSalt(resetPassword.getEmail());
		Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(resetPassword.getToken()).getSignature();
	    Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(salt.getBytes()).parseClaimsJws(resetPassword.getToken());        
		
	    return (resetPassword.getEmail()).equals(parseClaimsJws.getBody().getSubject()) ? true : false;
	}
	
	public String generateToken(String email) throws SQLException, OpumException {
		String salt = employeeRepository.retrieveSalt(email);
		
		Claims claims = Jwts.claims().setSubject(email);
        claims.put("salt", salt);
        Date currentTime = new Date();
        currentTime.setTime(currentTime.getTime() + 1440 * 60000);
        return Jwts.builder()
          .setClaims(claims)
          .setExpiration(currentTime)
          .signWith(SignatureAlgorithm.HS512, salt.getBytes())
          .compact();
	}
	
	private String generateEmailResetPasswordLink(String email) throws UnsupportedEncodingException {
		String resetPasswordHomeLink = "http://localhost:8080/online-pum-ui/admin/resetPasswordLink";
		String token = null;
		try {
			token = generateToken(email);
		} catch (SQLException | OpumException e) {
			e.printStackTrace();
		}
		return new StringBuilder(resetPasswordHomeLink)
				.append("?email=")
				.append(URLEncoder.encode(email, "UTF-8"))
				.append("&token=")
				.append(URLEncoder.encode(token, "UTF-8"))
				.toString();
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
