package com.ph.ibm.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmployeeReportDetails {
	
	private String projectName;
	private String serialNumber;
	private String resourceName;
	private String rollInDate;
	private String rollOffDate;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getRollInDate() {
		return rollInDate;
	}
	public void setRollInDate(String rollInDate) {
		this.rollInDate = rollInDate;
	}
	public String getRollOffDate() {
		return rollOffDate;
	}
	public void setRollOffDate(String rollOffDate) {
		this.rollOffDate = rollOffDate;
	}
	
}
