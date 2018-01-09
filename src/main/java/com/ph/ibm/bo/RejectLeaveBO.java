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

import com.ph.ibm.model.EmployeeUpdate;
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
			} else if (forRejectionEmailMap.containsKey(forRejection.getEmployee_Id())) {
				List<ForApproval> forRejectionListUpdated =
						forRejectionEmailMap.get(forRejection.getEmployee_Id());
				forRejectionListUpdated.add(forRejection);
				forRejectionEmailMap.put(forRejection.getEmployee_Id(), forRejectionListUpdated);
			}
		}

		return forRejectionEmailMap;
	}

	private ForRejectionEmail setupRejectEmailToUser(String employeedId, List<ForApproval> forRejectList) throws SQLException {
		ForRejectionEmail forRejectionEmail = new ForRejectionEmail();
		forRejectionEmail.setRecipientAddress(this.returnEmailAddressOfRecepient(forRejectList));
		forRejectionEmail.setSenderAddress( OpumConstants.EMAIL_SENDER );
		forRejectionEmail.setRecipientType( RecipientType.TO.toString() );
		forRejectionEmail.setSubject( OpumConstants.REJECT_EMAIL_SUBJECT );
		forRejectionEmail.setText( OpumConstants.EMAIL_GREETING + "<br><br>" +
				OpumConstants.REJECT_EMAIL_BODY + (forRejectList != null ?
						forRejectList.get(0).getUsernameForEmail() : OpumConstants.EMAIL_SIGNATURE) + "<br><br>" +
				createEmailLeavesTableReport(forRejectList));
		return forRejectionEmail;
	}

	private String createEmailLeavesTableReport(List<ForApproval> forRejectList) {
		StringBuilder datesRejectedsb = new StringBuilder("<table border=1>");
		datesRejectedsb.append("<tr>");
		datesRejectedsb.append("<th>Date</th>");
		datesRejectedsb.append("</tr>");
		for (ForApproval forApproval : forRejectList) {
			datesRejectedsb.append("<tr>");
			datesRejectedsb.append("<th>" + forApproval.getLeave_Date() +"</th>");
			datesRejectedsb.append("</tr>");
		}
		datesRejectedsb.append("</table>");
		return datesRejectedsb.toString();
	}

    private void sendRejectEmailToUsers(ForRejectionEmail emailToBeSentToUsers, Session session) {
    	try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailToBeSentToUsers.getSenderAddress()));
        message.setSubject(emailToBeSentToUsers.getSubject());
        message.setRecipient(Message.RecipientType.TO,
            new InternetAddress(emailToBeSentToUsers.getRecipientAddress()));
        message.setContent(emailToBeSentToUsers.getText() + "<br><br>"
            + OpumConstants.EMAIL_CLOSING + "<br>" + OpumConstants.EMAIL_SIGNATURE,"text/html");
        Transport.send(message);

      } catch (MessagingException e) {
    	  LOGGER.error("Error in sending email"+ e.getMessage());
      }
    }

    private String returnEmailAddressOfRecepient(List<ForApproval> forRejectList) throws SQLException {
    	String employeeId = "";
    	if (forRejectList != null) {
    		employeeId = forRejectList.get(0).getEmployee_Id();
    	}
    	EmployeeUpdate rejectedLeaveEmployee = this.employeeRepository.searchEmployee(employeeId);
    	return rejectedLeaveEmployee != null ? rejectedLeaveEmployee.getEmail() : "";
    }
}
