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

	
	
}
