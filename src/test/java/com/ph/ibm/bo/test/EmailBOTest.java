package com.ph.ibm.bo.test;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

import com.ph.ibm.bo.EmailBO;
import com.ph.ibm.model.Email;

public class EmailBOTest {

	EmailBO emailBO;

	@Test
	public void testFormatAndSendEmailPasswordReset() {
		emailBO = new EmailBO();
		Email email = new Email();
		List<String> recipientAddresses = new ArrayList<>();
		recipientAddresses.add("onlinepumsender@gmail.com");
		email.setRecipientAddresses(recipientAddresses);
		email.setRecipientType(RecipientType.TO.toString());
		email.setSenderAddress("onlinepumsender@gmail.com");
		email.setSubject("Testing");
		email.setText("Dear User," + "\n\n For testing, please!");
		
		emailBO.formatAndSendEmailPasswordReset(email);
	}
}
