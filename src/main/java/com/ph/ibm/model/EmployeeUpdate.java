package com.ph.ibm.model;

public class EmployeeUpdate extends BaseAuditBean{

	private int employeeId;
	private String employeeIdNumber;
	private String managerSerial;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	private String email;
	private String projectName;
	private boolean isAdmin;
	private boolean isActive;
	private String startDate;
	private String endDate;

	public EmployeeUpdate() {

	}

	public EmployeeUpdate(int employeeId, String employeeIdNumber, String firstName, String middleName, String lastName,
			String fullName, String email, String projectName, boolean isActive, boolean isAdmin, String startDate,
			String endDate) {

		this.employeeId = employeeId;
		this.employeeIdNumber = employeeIdNumber;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.email = email;
		this.projectName = projectName;
		this.isActive = isActive;
		this.isAdmin = isAdmin;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeIdNumber() {
		return employeeIdNumber;
	}

	public void setEmployeeIdNumber(String employeeIdNumber) {
		this.employeeIdNumber = employeeIdNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getManagerSerial() {
		return managerSerial;
	}

	public void setManagerSerial(String managerSerial) {
		this.managerSerial = managerSerial;
	}

}
