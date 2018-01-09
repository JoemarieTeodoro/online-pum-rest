package com.ph.ibm.bo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.ph.ibm.repository.EmployeeRepository;
import com.ph.ibm.repository.impl.EmployeeRepositoryImpl;
import com.ph.ibm.util.EmailUtils;
import com.ph.ibm.util.OpumConstants;

public class ApproveLeaveBO {

	private Logger LOGGER = Logger.getLogger(ApproveLeaveBO.class);

	private BluePagesBO bluePagesBO;

	private EmployeeRepository employeeRepository;

	public ApproveLeaveBO(){
		bluePagesBO = new BluePagesBO();
		employeeRepository = new EmployeeRepositoryImpl();
	}

	public ApproveLeaveBO( BluePagesBO bluePagesBO ){
		this.bluePagesBO = bluePagesBO;
		employeeRepository = new EmployeeRepositoryImpl();
	}

	public void sendAdminApproveEmail(List<ForApproval> forApprovalList) throws SQLException {
		LOGGER.debug("Sending approval email to admins started...");
		Email emailSendToAdmins = contructEmail( employeeRepository.getAdminEmailList() ,forApprovalList);
		sendApprovalEmail(emailSendToAdmins);
		LOGGER.debug("Sending approval email to admins done...");
	}

	public void sendPEMApproveEmail(List<ForApproval> forApprovalList) {
		LOGGER.debug("Sending approval email to PEMs started...");
		Map<String, List<ForApproval>> pemMap = getPemMap( forApprovalList );
		pemMap.entrySet().forEach(entry -> sendEmail(entry));
		LOGGER.debug("Sending approval email to PEMs done...");
	}

	protected void sendEmail(Entry<String, List<ForApproval>> entry) {
		Email email = contructEmail(Arrays.asList( entry.getKey() ) , entry.getValue());
		sendApprovalEmail(email);
	}

	protected Map<String, List<ForApproval>> getPemMap(List<ForApproval> forApprovalList) {
		Map<String, List<ForApproval>> map = new HashMap<String, List<ForApproval>>();
		forApprovalList.forEach(approval -> addToMap(map,approval));
		return map;
	}

	protected Map<String, List<ForApproval>> addToMap(Map<String, List<ForApproval>> map,ForApproval approval){
		String pemEmail = bluePagesBO.getPemEmailUsingEmployeeSerial( approval.getEmployee_Id() );
		if(map.containsKey(pemEmail)){
			List<ForApproval> approvalList = map.get(pemEmail);
			approvalList.add(approval);
			map.put(pemEmail, approvalList);
		}
		else {
			map.put(pemEmail, new ArrayList<>(Arrays.asList(approval)));
		}
		return map;
	}

	protected Email contructEmail(List<String> receipients, List<ForApproval> forApprovalList) {
		Email forApprovalEmail = new Email();
		forApprovalEmail.setRecipientAddresses(receipients);
		forApprovalEmail.setSenderAddress( OpumConstants.EMAIL_SENDER );
		forApprovalEmail.setRecipientType( RecipientType.TO.toString() );
		forApprovalEmail.setSubject( OpumConstants.APPROVE_EMAIL_SUBJECT );
		forApprovalEmail.setText( OpumConstants.EMAIL_GREETING + "<br><br>" +
				OpumConstants.APPROVE_EMAIL_BODY + (forApprovalList != null ?
					forApprovalList.get(0).getUsernameForEmail() : OpumConstants.EMAIL_SIGNATURE) + "<br><br>" +
				createEmailLeavesTableReport(forApprovalList));
		return forApprovalEmail;
	}

	protected String createEmailLeavesTableReport(List<ForApproval> forApprovalList) {
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

	protected void sendApprovalEmail( Email email ) {
		Session session = EmailUtils.createEmailSession();
    	try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email.getSenderAddress()));
			message.setSubject(email.getSubject());
			for (String recipientAddress : email.getRecipientAddresses()) {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
				message.setContent(email.getText() + "<br><br>"
						+ OpumConstants.EMAIL_CLOSING + "<br>" + OpumConstants.EMAIL_SIGNATURE, "text/html");
				Transport.send(message);
			}

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
}