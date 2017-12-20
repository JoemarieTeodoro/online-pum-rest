package com.ph.ibm.model;

import java.util.List;

public class ForApprovalEmail extends Email {

	private String employeeId;
	private List<String> employeeLeaves;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public List<String> getEmployeeLeaves() {
		return employeeLeaves;
	}

	public void setEmployeeLeaves(List<String> employeeLeaves) {
		this.employeeLeaves = employeeLeaves;
	}
}