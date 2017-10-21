package com.ph.ibm.validation.impl;

/**
 * This class contains regular expression patterns that validates format of
 * name, email address, employee id, project name
 */
public class FormatValidation {

	/**
	 * This method validate the format of employee name
	 * 
	 * @param employeeName
	 * @return true if name matches the regular expression pattern
	 */
	public boolean isValidEmployeeName(String name) {
		return name.matches("^([A-Za-z.]+[ ]{0,1})*([A-Za-z.]+[ ]{0,1})*$");
	}

	/**
	 * This method validate the format of employee email address
	 * 
	 * @param employeeEmail
	 * @return true if email address matches the regular expression pattern
	 */
	public boolean isValidEmailAddress(String emailAddress) {
		return emailAddress.matches("^[a-zA-Z0-9]+@+[a-zA-Z]{0,2}([\\.]+)+[ibm]+([\\.]+)([com]{3,})$");
	}

	/**
	 * This method validate the format of employee id
	 * 
	 * @param employeeId
	 * @return true if employee id matches the regular expression pattern
	 */
	public boolean isValidEmployeeId(String id) {
		return id.matches("^([A-Za-z0-9]+[ ]{0,1})*([A-Za-z0-9]+[ ]{0,1})*$");
	}

	/**
	 * This method validate the format of project name
	 * 
	 * @param projectName
	 * @return true if project name matches the regular expression pattern
	 */
	public boolean isValidProjectName(String projectName) {
		return projectName.matches("^[a-zA-Z0-9]{3,10}$");
	}

}
