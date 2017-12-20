package com.ph.ibm.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;

import com.ph.ibm.model.ForApproval;
import com.ph.ibm.model.ForRejectionEmail;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.util.EmailUtils;
import com.ph.ibm.util.OpumConstants;

public class RejectLeaveBO {

	private Logger LOGGER = Logger.getLogger(RejectLeaveBO.class);

	EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

	public void sendEmployeeRejectLeaveEmail(List<ForApproval> forRejectionList) throws SQLException {
		Session session = EmailUtils.createEmailSession();
		Map<String, List<ForApproval>> forRejectionEmailMap = this.deriveForRejectionEmailList(forRejectionList);
		for (Map.Entry<String, List<ForApproval>> forRejectionEntry : forRejectionEmailMap.entrySet()) {
		    ForRejectionEmail email =
		    		this.setupRejectEmailToUser(forRejectionEntry.getKey(), (forRejectionEntry.getValue()));
		    this.sendRejectEmailToUsers(email, session);
		}
	}

	private Map<String, List<ForApproval>> deriveForRejectionEmailList(List<ForApproval> forRejectionList) {
		Map<String, List<ForApproval>> forRejectionEmailMap = new HashedMap<>();
		for (ForApproval forRejection : forRejectionList) {
			List<ForApproval> forApprovalList;
			if (!forRejectionEmailMap.containsKey(forRejection.getEmployee_Id())) {
				forApprovalList = new ArrayList<>();
				forApprovalList.add(forRejection);
				forRejectionEmailMap.put(forRejection.getEmployee_Id(), forApprovalList);
			} else if (forRejectionEmailMap.containsKey(forRejection.getEmployee_Id()) &&
				mapKeyContainsExistingForApprovalObj(forRejectionEmailMap, forRejection)) {

				List<ForApproval> forRejectionListUpdated =
						forRejectionEmailMap.get(forRejection.getEmployee_Id());
				forRejectionListUpdated.add(forRejection);
				forRejectionEmailMap.put(forRejection.getEmployee_Id(), forRejectionListUpdated);
			}
		}

		return forRejectionEmailMap;
	}

	public boolean mapKeyContainsExistingForApprovalObj(Map<String, List<ForApproval>> forRejectionEmailMap,
			ForApproval forRejection) {
		boolean isMapKeyContainsExistingForApprovalObj = false;
		List<ForApproval> forApprovalList = forRejectionEmailMap.get(forRejection.getEmployee_Id());
		if (forApprovalList != null && forApprovalList.contains(forRejection)) {
			isMapKeyContainsExistingForApprovalObj = true;
		}
		return isMapKeyContainsExistingForApprovalObj;
	}

	private ForRejectionEmail setupRejectEmailToUser(String employeedId, List<ForApproval> forRejectList) throws SQLException {
		ForRejectionEmail forRejectionEmail = new ForRejectionEmail();
		forRejectionEmail.setRecipientAddress("rabangm@ph.ibm.com");
		forRejectionEmail.setSenderAddress( "onlinepumsender@gmail.com" );
		forRejectionEmail.setRecipientType( RecipientType.TO.toString() );
		forRejectionEmail.setSubject( OpumConstants.REJECT_EMAIL_SUBJECT );
		forRejectionEmail.setText( OpumConstants.EMAIL_GREETING + "\n\n" +
				OpumConstants.REJECT_EMAIL_BODY + forRejectList.get(0) + "\n\n" +
				createEmailLeavesTableReport(forRejectList));
		return forRejectionEmail;
	}

	private String createEmailLeavesTableReport(List<ForApproval> forRejectList) {
		StringBuilder datesRejectedsb = new StringBuilder();
		for (ForApproval forApproval : forRejectList) {
			datesRejectedsb.append(forApproval.getLeave_Date());
			datesRejectedsb.append("\n");
		}
		return datesRejectedsb.toString();
	}

    private void sendRejectEmailToUsers(ForRejectionEmail emailToBeSentToUsers, Session session) {
    	try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailToBeSentToUsers.getSenderAddress()));
			message.setSubject(emailToBeSentToUsers.getSubject());
			message.setRecipient(Message.RecipientType.TO,
					new InternetAddress(emailToBeSentToUsers.getRecipientAddress()));
			message.setText(emailToBeSentToUsers.getText() + "\n\n"
					+ OpumConstants.EMAIL_CLOSING + "\n" + OpumConstants.EMAIL_SIGNATURE);
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
}
