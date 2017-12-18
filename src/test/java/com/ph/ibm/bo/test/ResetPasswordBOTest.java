package com.ph.ibm.bo.test;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage.RecipientType;

import org.junit.Test;

import com.ph.ibm.bo.ProjectBO;
import com.ph.ibm.bo.ResetPasswordBO;
import com.ph.ibm.model.Email;

public class ResetPasswordBOTest {

	ResetPasswordBO resetPasswordBO;

	// test email credentials:
	//
	//   onlinepumsender@gmail.com : onlinepum
	//   onlinepumrecipient@gmail.com : onlinepum01
	
	public void testFormatAndSendEmailPasswordReset() throws Exception {
		resetPasswordBO = new ResetPasswordBO();
		Email email = new Email();
		List<String> recipientAddresses = new ArrayList<>();
		recipientAddresses.add("onlinepumsender@gmail.com");
		recipientAddresses.add("onlinepumrecipient@gmail.com");

		email.setRecipientAddresses(recipientAddresses);
		email.setRecipientType(RecipientType.TO.toString());
		email.setSenderAddress("onlinepumsender@gmail.com");
		email.setSubject("Testing");
		email.setText("Dear User," + "\n\n For testing, please! \n\n reset password link: %s");
		
        resetPasswordBO.emailResetPasswordLinkToSingleEmployee( email );
	}
	
/*	@Test
	public void generateHashedPassword() throws Exception {
		String hashed = MD5HashEncrypter.computeMD5Digest("onlinepum");
		System.out.print("hashed " + hashed);
	}*/
	
	@Test
	public void testEmailToListOfRecipients() throws Exception
	{
		resetPasswordBO = new ResetPasswordBO();
		ProjectBO projectBO = new ProjectBO();
		Email email = new Email();
		List<String> recipientAddresses = new ArrayList<>();
		recipientAddresses.add("onlinepumsender@gmail.com");
		recipientAddresses.add("onlinepumuser1@gmail.com");

		email.setRecipientAddresses(recipientAddresses);
		email.setSenderAddress("onlinepumsender@gmail.com");
	}

}
