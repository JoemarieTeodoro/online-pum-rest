package com.ph.ibm.model;

public class ForApproval {
	private int employee_Leave_Id;
	private String employee_Id;
	private String fullName;
	private String year_Id;
	private String status;
	private String leave_Date;
	private String leave_Type;
	private String create_Date;
	private String update_Date;
	private String team_Lead_Employee_Id;
	private String usernameForEmail;

	public ForApproval() {
	}

	public ForApproval(int employee_Leave_Id, String employee_Id, String fullName, String year_Id, String status, String leave_Date, String leave_Type,
			String create_Date, String update_Date, String team_Lead_Employee_Id) {
		super();
		this.employee_Leave_Id = employee_Leave_Id;
		this.employee_Id = employee_Id;
		this.fullName = fullName;
		this.year_Id = year_Id;
		this.status = status;
		this.leave_Date = leave_Date;
		this.leave_Type = leave_Type;
		this.create_Date = create_Date;
		this.update_Date = update_Date;
		this.team_Lead_Employee_Id = team_Lead_Employee_Id;

	}

	public int getEmployee_Leave_Id() {
		return employee_Leave_Id;
	}

	public void setEmployee_Leave_Id(int employee_Leave_Id) {
		this.employee_Leave_Id = employee_Leave_Id;
	}

	public String getEmployee_Id() {
		return employee_Id;
	}

	public void setEmployee_Id(String employee_Id) {
		this.employee_Id = employee_Id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getYear_Id() {
		return year_Id;
	}

	public void setYear_Id(String year_Id) {
		this.year_Id = year_Id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLeave_Date() {
		return leave_Date;
	}

	public void setLeave_Date(String leave_Date) {
		this.leave_Date = leave_Date;
	}

	public String getLeave_Type() {
		return leave_Type;
	}

	public void setLeave_Type(String leave_Type) {
		this.leave_Type = leave_Type;
	}

	public String getCreate_Date() {
		return create_Date;
	}

	public void setCreate_Date(String create_Date) {
		this.create_Date = create_Date;
	}

	public String getUpdate_Date() {
		return update_Date;
	}

	public void setUpdate_Date(String update_Date) {
		this.update_Date = update_Date;
	}

	public String getTeam_Lead_Employee_Id() {
		return team_Lead_Employee_Id;
	}

	public void setTeam_Lead_Employee_Id(String team_Lead_Employee_Id) {
		this.team_Lead_Employee_Id = team_Lead_Employee_Id;
	}

	public String getUsernameForEmail() {
		return usernameForEmail;
	}

	public void setUsernameForEmail(String usernameForEmail) {
		this.usernameForEmail = usernameForEmail;
	}
}
