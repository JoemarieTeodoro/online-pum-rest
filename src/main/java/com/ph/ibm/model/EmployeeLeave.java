package com.ph.ibm.model;

public class EmployeeLeave {

	private String employeeLeaveID;
	private String employeeID;
	private String yearID;
	private String status;
	private int value;
	private String leaveName;
	private String date;
	private String createDate;
	private String updateDate;
	private boolean isHoliday;
	private boolean isLocked;
	
	@Override
	public String toString()
	{
		return "Employee Leave ID: " + employeeLeaveID + "\n"+
				"Employee ID: " + employeeID + "\n"+ 
				"Year Id: " + yearID + "\n" +
				"Status: " + status + "\n" +
				"leave Type: " + leaveName + "\n" + 
				"leave date: " + date + "\n";
	}

	public String getEmployeeLeaveID() {
		return employeeLeaveID;
	}

	public void setEmployeeLeaveID(String employeeLeaveID) {
		this.employeeLeaveID = employeeLeaveID;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getYearID() {
		return yearID;
	}

	public void setYearID(String yearID) {
		this.yearID = yearID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLeaveName() {
		return leaveName;
	}

	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	public boolean isHoliday() {
		return isHoliday;
	}

	public void setHoliday(boolean isHoliday) {
		this.isHoliday = isHoliday;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
}
