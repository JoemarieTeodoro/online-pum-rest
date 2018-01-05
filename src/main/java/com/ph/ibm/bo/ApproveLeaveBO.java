package com.ph.ibm.bo;

import java.sql.SQLException;
import java.util.List;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.ph.ibm.model.Email;
import com.ph.ibm.model.ForApproval;
import com.ph.ibm.model.ForApprovalList;
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.util.EmailUtils;
import com.ph.ibm.util.OpumConstants;

public class ApproveLeaveBO {

	private Logger LOGGER = Logger.getLogger(ApproveLeaveBO.class);

	BluePagesBO bluePagesBO = new BluePagesBO();

	private EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();

	public void sendAdminApproveEmail(List<ForApproval> forApprovalList) throws SQLException {
		Session session = EmailUtils.createEmailSession();
		Email emailSendToAdmins = this.setupApprovalEmailToAdmins(forApprovalList);
		sendApprovalEmailToAdmins(emailSendToAdmins, session);
	}

	public void sendPEMApproveEmail(ForApprovalList forApprovalList) {
//		Session session = EmailUtils.createEmailSession();
//		Map<String, List<ForApproval>> forRejectionEmailMap = this.deriveForPemEmailList(forApprovalList);
//		for (Map.Entry<String, List<ForApproval>> forRejectionEntry : forRejectionEmailMap.entrySet()) {
//		    ForRejectionEmail email =
//		    		this.setupRejectEmailToUser(forRejectionEntry.getKey(), (forRejectionEntry.getValue()));
//		    this.sendRejectEmailToUsers(email, session);
//		}
	}

//	private Map<String, List<ForApproval>> deriveForRejectionEmailList(ForApprovalList forRejectionList) {
//		Map<String, List<ForApproval>> forRejectionEmailMap = new HashedMap<>();
//		for (ForApproval forRejection : forRejectionList.getForApprovalList()) {
//			List<ForApproval> forApprovalList;
//			if (!forRejectionEmailMap.containsKey(forRejection.getEmployee_Id())) {
//				forApprovalList = new ArrayList<>();
//				forApprovalList.add(forRejection);
//				forRejectionEmailMap.put(forRejection.getEmployee_Id(), forApprovalList);
//			} else if (forRejectionEmailMap.containsKey(forRejection.getEmployee_Id()) &&
////				mapKeyContainsExistingForApprovalObj(forRejectionEmailMap, forRejection)) {
//
//				List<ForApproval> forRejectionListUpdated =
//						forRejectionEmailMap.get(forRejection.getEmployee_Id());
//				forRejectionListUpdated.add(forRejection);
//				forRejectionEmailMap.put(forRejection.getEmployee_Id(), forRejectionListUpdated);
//			}
//		}
//
//		return forRejectionEmailMap;
//	}


	private Email setupApprovalEmailToAdmins(List<ForApproval> forApprovalList) throws SQLException {
		Email forApprovalEmail = new Email();
		forApprovalEmail.setRecipientAddresses(this.employeeRepository.getAdminEmailList());
		forApprovalEmail.setSenderAddress( "onlinepumsender@gmail.com" );
		forApprovalEmail.setRecipientType( RecipientType.TO.toString() );
		forApprovalEmail.setSubject( OpumConstants.APPROVE_EMAIL_SUBJECT );
		forApprovalEmail.setText( OpumConstants.EMAIL_GREETING + "<br><br>" +
				OpumConstants.APPROVE_EMAIL_BODY + forApprovalList.get(0) + "<br><br>" +
				createEmailLeavesTableReport(forApprovalList));
		return forApprovalEmail;
	}

	private String createEmailLeavesTableReport(List<ForApproval> forApprovalList) {
		StringBuilder emailApproveLeavesTable = new StringBuilder("<table border=1>");
		emailApproveLeavesTable.append("<tr>");
		emailApproveLeavesTable.append("<th>Employee ID</th>");
		emailApproveLeavesTable.append("<th>Full Name</th>");
		emailApproveLeavesTable.append("<th>Leave Date</th>");
		emailApproveLeavesTable.append("</tr>");
		for (ForApproval forApproval : forApprovalList) {
			emailApproveLeavesTable.append("<tr>");
			emailApproveLeavesTable.append("<th>" + forApproval.getEmployee_Id() +"</th>");
			emailApproveLeavesTable.append("<th>" + forApproval.getFullName() +"</th>");
			emailApproveLeavesTable.append("<th>" + forApproval.getLeave_Date() +"</th>");
			emailApproveLeavesTable.append("</tr>");
		}
		emailApproveLeavesTable.append("</table>");
		return emailApproveLeavesTable.toString();
	}

    private void sendApprovalEmailToAdmins(Email emailToBeSentToAdmin, Session session) {
    	try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailToBeSentToAdmin.getSenderAddress()));
			message.setSubject(emailToBeSentToAdmin.getSubject());
			for (String recipientAddress : emailToBeSentToAdmin.getRecipientAddresses()) {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
				message.setContent(emailToBeSentToAdmin.getText() + "<br><br>"
						+ OpumConstants.EMAIL_CLOSING + "<br>" + OpumConstants.EMAIL_SIGNATURE, "text/html");
				Transport.send(message);
			}

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
}