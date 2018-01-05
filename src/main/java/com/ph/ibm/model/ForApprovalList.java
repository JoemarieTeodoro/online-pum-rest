package com.ph.ibm.model;

import java.util.List;

public class ForApprovalList {
	
	private List<ForApproval> forApprovalList;
	

	public ForApprovalList(List<ForApproval> forApprovalList) {
		super();
		this.forApprovalList = forApprovalList;
	}

	public ForApprovalList() {
		// TODO Auto-generated constructor stub
	}

	public List<ForApproval> getForApprovalList() {
		return forApprovalList;
	}

	public void setForApprovalList(List<ForApproval> forApprovalList) {
		this.forApprovalList = forApprovalList;
	}
	
}
