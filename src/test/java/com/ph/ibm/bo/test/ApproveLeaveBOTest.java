package com.ph.ibm.bo.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ph.ibm.bo.ApproveLeaveBO;
import com.ph.ibm.model.ForApproval;

public class ApproveLeaveBOTest {

	ApproveLeaveBO approveeLeaveBO;

	@Test
	public void testSendAdminApproveEmail() throws Exception {
		approveeLeaveBO = new ApproveLeaveBO();
		List<ForApproval> forApprovalList = this.createForApprovalList();
		approveeLeaveBO.sendAdminApproveEmail(forApprovalList);
	}

	public List<ForApproval> createForApprovalList() {
		List<ForApproval> approvalList = new ArrayList<>();

		ForApproval firstApprovalObject = new ForApproval();
		firstApprovalObject.setEmployee_Id("PX100XJ");
		firstApprovalObject.setFullName("Melvin Rabang");
		firstApprovalObject.setLeave_Date("11/29/17");


		ForApproval secondApprovalObject = new ForApproval();
		secondApprovalObject.setEmployee_Id("PX100XA");
		secondApprovalObject.setFullName("Jerome DeGuzman");
		secondApprovalObject.setLeave_Date("1/29/17");

		approvalList.add(firstApprovalObject);
		approvalList.add(secondApprovalObject);

		return approvalList;
	}
}
