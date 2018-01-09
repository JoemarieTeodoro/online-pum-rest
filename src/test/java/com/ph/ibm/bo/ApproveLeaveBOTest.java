package com.ph.ibm.bo;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import com.ph.ibm.model.Email;
import com.ph.ibm.model.ForApproval;
import com.ph.ibm.util.OpumConstants;

public class ApproveLeaveBOTest {

	ApproveLeaveBO approveeLeaveBO;
	
	@Test
	public void testGetPEMMap() {
		BluePagesBO bluePagesBO = Mockito.mock(BluePagesBO.class);
		doReturn("tanjs@ph.ibm.com").when(bluePagesBO).getPemEmailUsingEmployeeSerial("PX100XJ");
		doReturn("tanjs@ph.ibm.com").when(bluePagesBO).getPemEmailUsingEmployeeSerial("PX100XA");
		approveeLeaveBO = new ApproveLeaveBO(bluePagesBO);
		assertTrue( approveeLeaveBO.getPemMap(createForApprovalList()).keySet().contains("tanjs@ph.ibm.com") );
	}
	
	@Test
	public void testConstructEmail() {
		approveeLeaveBO = new ApproveLeaveBO();
		Email email = approveeLeaveBO.contructEmail(Arrays.asList("Jason"), createForApprovalList());
		assertTrue( email.getRecipientAddresses().contains( "Jason" ));
		assertTrue( email.getSubject().contains( OpumConstants.APPROVE_EMAIL_SUBJECT ));
		assertTrue( email.getSenderAddress().contains( "onlinepumsender@gmail.com" ));
		assertTrue( email.getText().contains( "DEAR IBMer,<br><br>Below is a table that represents "
											+ "the leaves approved today. They were approved by Jason Tan<br><br>"
											+ "<table border=1><tr><th>Employee ID</th><th>Full Name</th><th>Leave"
											+ " Date</th></tr><tr><th>PX100XJ</th><th>Melvin Rabang</th><th>11/29/"
											+ "17</th></tr><tr><th>PX100XA</th><th>Jerome DeGuzman</th><th>1/29/17"
											+ "</th></tr></table>" ));
	}
	
	@After
	public void teardown() {
		approveeLeaveBO = null;
	}

	public List<ForApproval> createForApprovalList() {
		List<ForApproval> approvalList = new ArrayList<>();

		ForApproval firstApprovalObject = new ForApproval();
		firstApprovalObject.setEmployee_Id("PX100XJ");
		firstApprovalObject.setFullName("Melvin Rabang");
		firstApprovalObject.setLeave_Date("11/29/17");
		firstApprovalObject.setUsernameForEmail("Jason Tan");


		ForApproval secondApprovalObject = new ForApproval();
		secondApprovalObject.setEmployee_Id("PX100XA");
		secondApprovalObject.setFullName("Jerome DeGuzman");
		secondApprovalObject.setLeave_Date("1/29/17");
		secondApprovalObject.setUsernameForEmail("Jason Tan");

		approvalList.add(firstApprovalObject);
		approvalList.add(secondApprovalObject);

		return approvalList;
	}
}
