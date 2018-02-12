package com.ph.ibm.report.pum.data;

public class PUMEmployeeData {
	
	public String projectName;
	
	public String serialNumber;
	
	public String resourceName;
	
	public String rollInData;

	public String rollOffData;
	
	public PUMOverallUtilizationData UtilData;

	public PUMEmployeeData(String projectName, String serialNumber, String resourceName, String rollInData,
			String rollOffData, PUMOverallUtilizationData utilData) {
		this.projectName = projectName;
		this.serialNumber = serialNumber;
		this.resourceName = resourceName;
		this.rollInData = rollInData;
		this.rollOffData = rollOffData;
		UtilData = utilData;
	}

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

	public String getRollInData() {
		return rollInData;
	}

	public void setRollInData(String rollInData) {
		this.rollInData = rollInData;
	}

	public String getRollOffData() {
		return rollOffData;
	}

	public void setRollOffData(String rollOffData) {
		this.rollOffData = rollOffData;
	}

	public PUMOverallUtilizationData getUtilData() {
		return UtilData;
	}

	public void setUtilData(PUMOverallUtilizationData utilData) {
		UtilData = utilData;
	}
}
