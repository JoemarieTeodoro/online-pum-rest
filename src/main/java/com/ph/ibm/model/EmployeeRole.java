package com.ph.ibm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmployeeRole {

	private String employeeSerial;
	private String employeeRoleString;
	private Role employeeRoleEnum;

	public String getEmployeeSerial() {
		return employeeSerial;
	}

	public void setEmployeeSerial(String employeeSerial) {
		this.employeeSerial = employeeSerial;
	}

	public String getEmployeeRoleString() {
		return employeeRoleString;
	}

	public void setEmployeeRoleString(String employeeRoleString) {
		this.employeeRoleString = employeeRoleString;
	}

	public Role getEmployeeRoleEnum() {
		return employeeRoleEnum;
	}

	public void setEmployeeRoleEnum(Role employeeRoleEnum) {
		this.employeeRoleEnum = employeeRoleEnum;
	}
	
    @Override
    public String toString(){
    	return String.format( "Employee Role!\n\nSerial No: %s \nRole: %s \nRole Enum: ",
    			this.getEmployeeSerial(), this.getEmployeeRoleString());
    }
}
