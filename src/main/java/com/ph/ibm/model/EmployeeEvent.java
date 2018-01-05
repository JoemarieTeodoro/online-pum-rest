package com.ph.ibm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmployeeEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<EmployeeLeave> empLeaveList = new ArrayList<EmployeeLeave>();
	private String currFYStartDate;
	private String curreFYEndDate;
	private boolean isDraft;
	private String empID;
	private String fyID;
	
	public String getStart() {
		return currFYStartDate;
	}
	public void setStart(String start) {
		this.currFYStartDate = start;
	}
	public String getEnd() {
		return curreFYEndDate;
	}
	public void setEnd(String end) {
		this.curreFYEndDate = end;
	}
	public List<EmployeeLeave> getEmpLeaveList() {
		return empLeaveList;
	}
	public void setEmpLeaveList(List<EmployeeLeave> empLeaveList) {
		this.empLeaveList = empLeaveList;
	}
	public String getCurrFYStartDate() {
		return currFYStartDate;
	}
	public void setCurrFYStartDate(String currFYStartDate) {
		this.currFYStartDate = currFYStartDate;
	}
	public String getCurrFYEndDate() {
		return curreFYEndDate;
	}
	public void setCurrFYEndDate(String curreFYEndDate) {
		this.curreFYEndDate = curreFYEndDate;
	}
	public boolean isDraft() {
		return isDraft;
	}
	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public String getFyID() {
		return fyID;
	}
	public void setFyID(String fyID) {
		this.fyID = fyID;
	}
	
}
